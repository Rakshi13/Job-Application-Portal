package com.rakshith.JobApplication.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CandidateRegisterRequest {

    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @NotBlank(message = "email Name cannot be blank")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotBlank(message = "No cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobile;

    @NotBlank(message = "User Name cannot be blank")
    @Size(min = 3,max = 20)
    private String username;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8,message = "Password should contain atleast 8 characters")
    private String password;
}
