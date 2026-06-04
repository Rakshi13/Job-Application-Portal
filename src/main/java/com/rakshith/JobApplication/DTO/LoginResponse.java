package com.rakshith.JobApplication.DTO;

import lombok.Data;

@Data
public class LoginResponse {

    private String username;
    private String role;
    private String message;
}
