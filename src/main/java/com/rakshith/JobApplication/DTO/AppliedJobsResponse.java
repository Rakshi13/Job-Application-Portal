package com.rakshith.JobApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppliedJobsResponse {

    private String title;

    private String location;

    private String companyName;

    private String status;

    private LocalDateTime appliedDate;

}
