package org.example.footballanalyzer.Data.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStaff {
    private long id;
    private String login;
    private String firstName;
    private String lastName;
    private String roleName;
}
