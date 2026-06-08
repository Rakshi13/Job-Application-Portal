package com.rakshith.JobApplication.DTO;

import com.rakshith.JobApplication.Enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Please enter valid Username")
    private String username;

    @NotBlank(message = "Password cannot be Empty.")
    private String password;

    private Role role;
}
