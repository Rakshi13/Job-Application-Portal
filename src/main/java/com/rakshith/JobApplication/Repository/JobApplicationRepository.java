package com.rakshith.JobApplication.Repository;

import com.rakshith.JobApplication.DTO.AppliedJobsResponse;
import com.rakshith.JobApplication.Entity.Candidate;
import com.rakshith.JobApplication.Entity.Job;
import com.rakshith.JobApplication.Entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {

    boolean existsByCandidateAndJob(Candidate candidate, Job job);

    List<JobApplication> findByCandidate(Candidate candidate);
}
