package org.example.footballanalyzer.Data.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "resetoperations")
public class ResetOperations {
    @Id
    @GeneratedValue(generator = "reset_operations_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "reset_operations_id_seq", sequenceName = "reset_operations_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "users")
    private UserEntity user;
    private String uuid;
    @Column(name = "createdate")
    private String createDate;

}
