package com.rakshith.JobApplication.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Each Application belongs to one candidate.
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    //Each Application belongs to one job.
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    private String status;

    private LocalDateTime appliedDate;
}
