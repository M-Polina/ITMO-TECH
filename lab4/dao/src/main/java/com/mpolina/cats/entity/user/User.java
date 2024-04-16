package com.mpolina.cats.entity.user;

import com.mpolina.cats.entity.owner.Owner;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;

    public User(){
    }

    public User(String username, String password, UserRole role, Owner owner) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.owner = owner;
    }
}

