package ru.nsu.fit.borzov.crocodile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "room")
    private List<User> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "drawer_id")
    private User drawer;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> wordToChoose = new ArrayList<>();

    @JsonIgnore
    private String word;

    public Room() {

    }

    public Room(String name) {
        this.name = name;
    }
}
