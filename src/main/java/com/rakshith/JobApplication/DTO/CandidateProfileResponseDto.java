package com.rakshith.JobApplication.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileResponseDto {

    private String fullName;
    private String location;
    private String skills;
    private String phoneNo;
    private Integer experience;
    private String currentDesignation;
    private String email;
    private String professionalSummary;

}
