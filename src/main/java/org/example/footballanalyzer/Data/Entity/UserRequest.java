package org.example.footballanalyzer.Data.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String requestType;
    private String requestStatus;
    private String requestData;
}
