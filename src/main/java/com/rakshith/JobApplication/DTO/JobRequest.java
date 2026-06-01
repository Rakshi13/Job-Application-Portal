package com.rakshith.JobApplication.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class JobRequest {

    @NotBlank(message = "Job Title cannot ba Empty.")
    private String title;

    @NotBlank(message = "Job Description cannot be Empty.")
    private String description;

    @NotNull(message = "Job Max Salary cannot be null.")
    private Long maxSalary;

    @NotNull(message = "Job Min Salary cannot be null")
    private Long minSalary;

    @NotBlank(message = "Job Location cannot be empty.")
    private String location;

    @NotNull(message = "Company Id cannot be null")
    private Long companyId;
}
