package ru.nsu.fit.borzov.crocodile.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.nsu.fit.borzov.crocodile.exception.AuthenticationException;
import ru.nsu.fit.borzov.crocodile.exception.UserNotFoundException;
import ru.nsu.fit.borzov.crocodile.model.User;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenUtil {
    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hour

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(login)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(algorithm);
    }

    public Authentication validateToken(UserService userService, String token) throws AuthenticationException {
        var decodedToken = decodeToken(token);

        try {
            User user = userService.findByName(decodedToken.getSubject());
            return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        } catch (UserNotFoundException e) {
            throw new AuthenticationException();
        }

    }

    public DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm)
                .build();

        return verifier.verify(token);
    }

}
