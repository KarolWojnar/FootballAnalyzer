package org.example.footballanalyzer.Data.Entity;

import lombok.Data;
import org.example.footballanalyzer.Data.Code;

import java.sql.Timestamp;

@Data
public class AuthResponse {
    private final String timestamp;
    private final String message;
    private final Code code;

    public AuthResponse(Code code) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = code.label;
        this.code = code;
    }


}
