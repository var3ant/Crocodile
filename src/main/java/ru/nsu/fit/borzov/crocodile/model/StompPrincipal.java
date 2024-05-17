package ru.nsu.fit.borzov.crocodile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;

@Getter
@AllArgsConstructor
public class StompPrincipal implements Principal {
    String name;
}
