package org.example.footballanalyzer.Data.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;
    private long teamId;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

}
