package org.example.footballanalyzer.Data.Entity;

import lombok.Data;
import org.example.footballanalyzer.Data.RoleName;

import javax.persistence.*;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName roleName;
}

