package ru.nsu.fit.borzov.crocodile.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private User requestFrom;

    @ManyToOne
    private User requestTo;

    public FriendRequest(User from, User to) {
        this.requestFrom = from;
        this.requestTo = to;
    }
}
