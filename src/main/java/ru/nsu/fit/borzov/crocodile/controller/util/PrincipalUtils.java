package ru.nsu.fit.borzov.crocodile.controller.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.nsu.fit.borzov.crocodile.model.User;

import java.security.Principal;

@Component
public class PrincipalUtils {

    public long getUserId(Principal principal) {
        var authenticationToken = (UsernamePasswordAuthenticationToken) principal;
        var user = (User) authenticationToken.getPrincipal();
        return user.getId();
    }
}
