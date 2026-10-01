package com.rakshith.JobApplication.Repository;

import com.rakshith.JobApplication.Entity.Candidate;
import com.rakshith.JobApplication.Entity.CandidateProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile,Long> {

    boolean existsByCandidate(Candidate candidate);

    Optional<CandidateProfile> findByCandidate_Id(Long candidateId);
}
