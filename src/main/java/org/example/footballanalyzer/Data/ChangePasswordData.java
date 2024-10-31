package org.example.footballanalyzer.Data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordData {
    private String password;
    private String uuid;
}
