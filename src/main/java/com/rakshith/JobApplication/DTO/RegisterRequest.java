package com.rakshith.JobApplication.DTO;

import com.rakshith.JobApplication.Enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
