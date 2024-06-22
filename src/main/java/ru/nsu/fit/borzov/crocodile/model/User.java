package ru.nsu.fit.borzov.crocodile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity(name = "user_data")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;

    @Size(max = 20)
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="room_id")
    private Room room;


    @JsonIgnore
    @Column(nullable = false)
    @Size(max = 50)
    private String password;

    public User() {

    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }


    public long getRoomId() {
        return room.getId();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("User"));
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return getName();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @ManyToMany
    @JoinTable(name = "user_data__friends",
            joinColumns =@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"
            ))
    private Set<User> friends = new HashSet<>();

    @OneToMany(mappedBy = "to")
    private Set<FriendRequest> incomingRequest = new HashSet<>();

    @OneToMany(mappedBy = "from")
    private Set<FriendRequest> outcomingRequest = new HashSet<>();
}
