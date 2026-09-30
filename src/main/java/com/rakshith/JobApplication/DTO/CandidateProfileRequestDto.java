package com.rakshith.JobApplication.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateProfileRequestDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Skills are required")
    private String skills;

    @NotBlank(message = "Phone number is required")
    private String phoneNo;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experience;

    private String currentDesignation;

    @NotBlank(message = "Company email cannot be empty.")
    @Email(message = "Enter a valid email address")
    private String email;

    @Size(max = 1000, message = "Summary cannot exceed 1000 characters")
    private String professionalSummary;
}
