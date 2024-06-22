package ru.nsu.fit.borzov.crocodile.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ConcurrentRandomService {
    public int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
}
