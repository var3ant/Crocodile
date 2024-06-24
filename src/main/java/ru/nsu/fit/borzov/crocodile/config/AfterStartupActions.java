package ru.nsu.fit.borzov.crocodile.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.nsu.fit.borzov.crocodile.model.GuessingPhrase;
import ru.nsu.fit.borzov.crocodile.repository.GuessingPhraseRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@RequiredArgsConstructor
public class AfterStartupActions {
    private final Logger logger = getLogger(AfterStartupActions.class);

    private final GuessingPhraseRepository guessingPhraseRepository;

    @Value("classpath:phrases_ru.txt")
    private Resource phrasesResource;

    //TODO: это нужно например через flyway написать, но пока так
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        if (guessingPhraseRepository.count() != 0) {
            return;
        }

        try {
            logger.info("Start loading phrases into the Database");

            var phrasesReader = new BufferedReader(new InputStreamReader(phrasesResource.getInputStream()));
            phrasesReader.lines().forEach((phrase) -> guessingPhraseRepository.save(new GuessingPhrase(phrase)));

            logger.info("Phrases were successfully loaded into the Database");

        } catch (IOException e) {
            logger.error("Phrases cannot be loaded. Reading error.");
        }
    }
}
