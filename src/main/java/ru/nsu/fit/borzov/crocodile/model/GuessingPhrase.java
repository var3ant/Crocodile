package ru.nsu.fit.borzov.crocodile.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GuessingPhrase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;

    private String phrase;

    public GuessingPhrase() {
    }

    public GuessingPhrase(String phrase) {
        this.phrase = phrase;
    }
}
