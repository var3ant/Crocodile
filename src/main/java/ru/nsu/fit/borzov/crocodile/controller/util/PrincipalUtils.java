package ru.nsu.fit.borzov.crocodile.controller.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.nsu.fit.borzov.crocodile.model.User;

import java.security.Principal;

public class PrincipalUtils {
    public static Long getUserId(Principal principal) {
        var authenticationToken = (UsernamePasswordAuthenticationToken) principal;
        var user = (User) authenticationToken.getPrincipal();
        return user.getId();
    }
}
