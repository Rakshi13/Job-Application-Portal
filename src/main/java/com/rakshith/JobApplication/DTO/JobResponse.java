package com.rakshith.JobApplication.DTO;
import lombok.Data;


@Data
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private Long minSalary;
    private Long maxSalary;
    private String location;
    private Long companyId;
    private String companyName;
}
