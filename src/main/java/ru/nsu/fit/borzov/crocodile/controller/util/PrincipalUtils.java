package ru.nsu.fit.borzov.crocodile.controller.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.nsu.fit.borzov.crocodile.exception.AuthenticationException;
import ru.nsu.fit.borzov.crocodile.model.User;

import java.security.Principal;

@Component
public class PrincipalUtils {

    public long getUserId(Principal principal) throws AuthenticationException {
        var authenticationToken = (UsernamePasswordAuthenticationToken) principal;
        if(authenticationToken == null) {
            throw new AuthenticationException();
        }

        var user = (User) authenticationToken.getPrincipal();
        return user.getId();
    }
}
