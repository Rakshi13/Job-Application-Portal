package com.rakshith.JobApplication.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyRequest {

    @Size(min = 2,max = 50,message = "Company name should be between the 2 and 50.")
    private String name;

    @NotBlank(message = "Company description cannot be empty.")
    private String description;
}
