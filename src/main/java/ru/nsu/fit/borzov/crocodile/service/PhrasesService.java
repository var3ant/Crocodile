package ru.nsu.fit.borzov.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nsu.fit.borzov.crocodile.exception.InternalServerErrorException;
import ru.nsu.fit.borzov.crocodile.model.GuessingPhrase;
import ru.nsu.fit.borzov.crocodile.model.Room;
import ru.nsu.fit.borzov.crocodile.repository.GuessingPhraseRepository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhrasesService {
    private final Logger logger = LoggerFactory.getLogger(PhrasesService.class);

    private final ConcurrentRandomService random;

    private final GuessingPhraseRepository guessingPhraseRepository;

    public List<String> generatePhraseToChoose(@NotNull Room room) {
        var count = guessingPhraseRepository.count();

        var phrases = new ArrayList<String>();

        for (int i = 0; i < 3; i++) {
            var idx = random.nextInt((int) count);
            Page<GuessingPhrase> phrasePage = guessingPhraseRepository.findAll(PageRequest.of(idx, 1));
            var phraseOpt = phrasePage.stream().findFirst();
            if (phraseOpt.isPresent()) {
                phrases.add(phraseOpt.get().getPhrase());
            } else {
                logger.error("Phrases cannot be received");
                throw new InternalServerErrorException();
            }
        }

        var phrasesInOneString = StringUtils.join(phrases, ',');
        logger.info("Phrases for room {}:{}", room.getId(), phrasesInOneString);
        return phrases;
    }
}
