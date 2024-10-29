package org.example.footballanalyzer.Data.Dto;

import lombok.Data;

@Data
public class UserRequesetDto {
    private String login;
    private String requestType;
    private String requestStatus;
    private Object requestData;
}
