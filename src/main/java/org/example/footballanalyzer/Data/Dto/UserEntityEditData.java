package org.example.footballanalyzer.Data.Dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

@Builder
@Data
public class UserEntityEditData {
    @Nullable
    private Long id;
    @Nullable
    private String login;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String email;
    @Nullable
    private Long roleId;
    @Nullable
    private String roleName;
    @Nullable
    private String teamName;
    @Nullable
    private Long teamId;
    @Nullable
    private String password;
    @Nullable
    private boolean hasPdf;
}
