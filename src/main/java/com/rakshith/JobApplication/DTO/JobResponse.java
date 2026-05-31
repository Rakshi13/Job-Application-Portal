package com.rakshith.JobApplication.DTO;
import lombok.Data;


@Data
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private Long maxSalary;
    private Long minSalary;
    private String location;

    private Long companyId;
    private String companyName;
}
