package org.example.footballanalyzer.Data.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
@Builder
public class UserDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long teamId;
    private String teamLogo;
    private String login;
    @Length(min = 8, message = "Hasło musi mieć minimum 8 znaków.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long roleId;
    private String roleName;
}
