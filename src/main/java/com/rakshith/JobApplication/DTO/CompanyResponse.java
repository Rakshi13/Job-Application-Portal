package com.rakshith.JobApplication.DTO;

import lombok.Data;

@Data
public class CompanyResponse {

    private Long id;
    private String name;
    private String description;

    private int totalJobs;
    private int totalReviews;
}
