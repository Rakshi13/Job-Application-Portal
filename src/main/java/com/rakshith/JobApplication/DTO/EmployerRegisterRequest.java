package com.rakshith.JobApplication.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployerRegisterRequest {
    @NotBlank(message = "Company Name cannot be blank")
    private String companyName;

    @NotBlank(message = "email Name cannot be blank")
    @Email(message = "Invalid Email Format")
    private String companyEmail;

    @NotBlank(message = "Company Website cannot be blank")
    private String companyWebsite;

    @NotBlank(message = "HR Name cannot be blank")
    private String HrName;

    @NotBlank(message = "User Name cannot be blank")
    @Size(min = 3,max = 20)
    private String username;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8,message = "Password should contain atleast 8 characters")
    private String password;
}
