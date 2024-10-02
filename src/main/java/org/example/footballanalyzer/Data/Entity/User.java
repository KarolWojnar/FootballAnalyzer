package org.example.footballanalyzer.Data.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @ManyToOne
    private Role role;
    @OneToOne
    private Team team;
}
