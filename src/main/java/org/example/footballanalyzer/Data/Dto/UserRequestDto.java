package org.example.footballanalyzer.Data.Dto;

import lombok.Builder;
import lombok.Data;
import org.example.footballanalyzer.Data.Entity.UserRequest;

import javax.persistence.Column;

@Builder
@Data
public class UserRequestDto {
    private Long id;
    private Long userId;
    private String login;
    private String requestType;
    private UserRequest.RequestStatus requestStatus;
    private Object requestData;
}
