package ru.nsu.fit.borzov.crocodile.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    private User from;

    @ManyToOne
    private User to;

    public FriendRequest(User from, User to) {
        this.from = from;
        this.to = to;
    }
}
