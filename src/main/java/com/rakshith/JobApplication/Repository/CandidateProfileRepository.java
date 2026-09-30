package com.rakshith.JobApplication.Repository;

import com.rakshith.JobApplication.Entity.Candidate;
import com.rakshith.JobApplication.Entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile,Long> {

    boolean existsByCandidate(Candidate candidate);
}
