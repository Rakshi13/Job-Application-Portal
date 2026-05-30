package com.rakshith.JobApplication.DTO;
import lombok.Data;


@Data
public class JobRequest {
    private String title;
    private String description;
    private Long maxSalary;
    private Long minSalary;
    private String location;

    private Long companyId;
}
