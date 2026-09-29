package com.rakshith.JobApplication.Repository;

import com.rakshith.JobApplication.Entity.Candidate;
import com.rakshith.JobApplication.Entity.Job;
import com.rakshith.JobApplication.Entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob,Long> {

    boolean existsByCandidateAndJob(Candidate candidate, Job job);

    List<SavedJob> findByCandidate(Candidate candidate);
}
