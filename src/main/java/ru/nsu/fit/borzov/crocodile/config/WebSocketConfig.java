package ru.nsu.fit.borzov.crocodile.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.nsu.fit.borzov.crocodile.exception.AuthenticationException;
import ru.nsu.fit.borzov.crocodile.service.JwtTokenUtil;
import ru.nsu.fit.borzov.crocodile.service.UserService;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/game").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel unused) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                assert accessor != null;

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    try {
                        var authentication = getAuthenticationByAccessor(accessor);

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        accessor.setUser(authentication);
                    } catch (AuthenticationException e) {
                        SecurityContextHolder.clearContext();
                        accessor.setUser(null);
                    }
                }

                return message;
            }

        });
    }

    private Authentication getAuthenticationByAccessor(StompHeaderAccessor accessor) throws AuthenticationException {
        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
        if (authorizationHeader == null) {
            throw new AuthenticationException();
        }
        String token = authorizationHeader.substring(7);

        return jwtTokenUtil.validateToken(userService, token);
    }
}
