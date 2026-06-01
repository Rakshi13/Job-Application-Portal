package com.rakshith.JobApplication.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewRequest {

    @NotBlank(message = "Review Title should not be blank.")
    private String title;

    @NotBlank(message = "Review Description cannot be empty")
    private String description;

    @Min(value = 1,message = "Review rating should be at least should be 1.")
    @Max(value = 5,message = "Review Rating should not exceed 5.")
    private double rating;
}
