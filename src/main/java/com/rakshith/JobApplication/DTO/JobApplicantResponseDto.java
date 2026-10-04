package com.rakshith.JobApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicantResponseDto {
    private Long applicationId;
    private Long candidateId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String location;
    private String skills;
    private String resumeFileName;
    private LocalDateTime appliedAt;
}
