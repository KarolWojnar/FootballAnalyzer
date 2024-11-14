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
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    private String requestData;

    public enum RequestStatus {
        NOWE,
        ROZWIĄZANE,
        BRAK_ROZWIĄZANIA,
        ZAMKNIĘTE,
        ODRZUCONE
    }
}



