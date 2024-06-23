package ru.nsu.fit.borzov.crocodile.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import ru.nsu.fit.borzov.crocodile.model.GuessingPhrase;
import ru.nsu.fit.borzov.crocodile.repository.GuessingPhraseRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
@RequiredArgsConstructor
public class AfterStartupActions {
    private final GuessingPhraseRepository guessingPhraseRepository;
    private static final String PHRASES_FILENAME = "classpath:phrases_ru.txt";

    private final Logger logger = getLogger(AfterStartupActions.class);

    //TODO: это нужно например через flyway написать, но пока так
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        if (guessingPhraseRepository.count() != 0) {
            return;
        }

        try {
            logger.info("Start loading phrases into the Database");
            var file = ResourceUtils.getFile(PHRASES_FILENAME);
            List<String> phrases = Files.readAllLines(file.toPath(), Charset.defaultCharset());
            for (String phrase : phrases) {
                var phraseModel = new GuessingPhrase();
                phraseModel.setPhrase(phrase);

                guessingPhraseRepository.save(phraseModel);
            }
            logger.info("Phrases were successfully loaded into the Database");

        } catch (IOException e) {
            logger.error("Phrases cannot be loaded. Reading error.");
        }
    }
}
