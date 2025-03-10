package org.example.footballanalyzer.Data.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    @Column(unique = true)
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Builder.Default
    @Column(name = "islock")
    private boolean isLocked = true;
    @Builder.Default
    @Column(name = "isenabled")
    private boolean isEnabled = true;
    @ManyToOne
    private Role role;
    @ManyToOne
    private Team team;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRequest> userRequest;

    public UserEntity(Long id, String uuid, String login, String password, String firstName,
                      String lastName, String email, boolean isLocked, boolean isEnabled,
                      Role role, Team team, Set<UserRequest> userRequest, byte[] coachConfirmPdf) {
        this.id = id;
        this.uuid = uuid;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isLocked = isLocked;
        this.isEnabled = isEnabled;
        this.role = role;
        this.team = team;
        this.userRequest = userRequest;
        this.coachConfirmPdf = coachConfirmPdf;
        generateUuid();
    }

    private void generateUuid() {
        if (uuid == null || uuid.isEmpty()) {
            setUuid(UUID.randomUUID().toString());
        }
    }

    @Lob
    private byte[] coachConfirmPdf;
}
