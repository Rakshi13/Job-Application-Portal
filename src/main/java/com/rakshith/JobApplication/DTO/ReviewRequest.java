package com.rakshith.JobApplication.DTO;

import lombok.Data;

@Data
public class ReviewRequest {

    private String title;
    private String description;
    private double rating;
}
