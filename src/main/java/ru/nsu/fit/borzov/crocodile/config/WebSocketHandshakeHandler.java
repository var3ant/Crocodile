package ru.nsu.fit.borzov.crocodile.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import ru.nsu.fit.borzov.crocodile.model.StompPrincipal;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;

@Slf4j
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

//    @Override
//    protected Principal determineUser(
//            ServerHttpRequest request,
//            WebSocketHandler wsHandler,
//            Map<String, Object> attributes) {
//        String rawCookie = request.getHeaders().get("cookie").get(0);
//        rawCookie = java.net.URLDecoder.decode(rawCookie, StandardCharsets.UTF_8);
//        String[] rawCookieParams = rawCookie.split(";");
//        for (String rawCookieNameAndValue : rawCookieParams) {
//            String[] rawCookieNameAndValuePair = rawCookieNameAndValue.split("=");
//            if ("Login".equals(rawCookieNameAndValuePair[0].strip())) {
//                return new StompPrincipal(rawCookieNameAndValuePair[1]);
//            }
//        }
//
//        return new StompPrincipal("Unknown");
//    }

    @Override
    protected Principal determineUser(
            ServerHttpRequest request,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        String[] cookies = request.getHeaders().get("cookie").get(0).split(";");
        var id = "Unknown";
        for (String cookie : cookies) {
            if(cookie.contains("userId=")) {
                id = cookie.substring(cookie.indexOf("=") + 1);
                break;
            }
        }
        return new StompPrincipal(id);
    }
}
