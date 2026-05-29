package com.rakshith.JobApplication.DTO;


import lombok.Data;

import java.util.Objects;

@Data
public class JobResponse {

    private String title;
    private String description;
    private Long maxSalary;
    private Long minSalary;
    private String location;
}
