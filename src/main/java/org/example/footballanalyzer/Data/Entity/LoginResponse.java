package org.example.footballanalyzer.Data.Entity;

import lombok.Data;
import org.example.footballanalyzer.Data.Code;

import java.sql.Timestamp;

@Data
public class LoginResponse {
    private final String timestamp;
    private final boolean message;
    private final Code code;

    public LoginResponse(boolean message) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = message;
        this.code = Code.SUCCESS;
    }
}
