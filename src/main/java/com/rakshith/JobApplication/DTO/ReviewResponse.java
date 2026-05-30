package com.rakshith.JobApplication.DTO;

import lombok.Data;

@Data
public class ReviewResponse {
    private Long id;
    private String title;
    private String description;
    private double rating;

    private Long companyId;
    private String companyName;
}
