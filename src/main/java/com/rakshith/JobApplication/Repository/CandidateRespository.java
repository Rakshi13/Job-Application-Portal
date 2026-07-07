package com.rakshith.JobApplication.Repository;

import com.rakshith.JobApplication.Entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRespository extends JpaRepository<Candidate,Long> {
}
