package ru.nsu.fit.borzov.crocodile.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.borzov.crocodile.model.GuessingPhrase;

@Repository
public interface GuessingPhraseRepository extends JpaRepository<GuessingPhrase, Long> {
}
