package com.rakshith.JobApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedJobsResponseDto {
    private String title;
    private String description;
    private Long minSalary;
    private Long maxSalary;
    private String location;
    private String companyName;
}
