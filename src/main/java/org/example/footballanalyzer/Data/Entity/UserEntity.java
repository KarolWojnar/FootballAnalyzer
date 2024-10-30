package org.example.footballanalyzer.Data.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(name = "logindisabled")
    private boolean loginDisabled;
    @ManyToOne
    private Role role;
    @ManyToOne
    private Team team;
    @OneToMany(mappedBy = "user")
    private Set<UserRequest> userRequest;
}
