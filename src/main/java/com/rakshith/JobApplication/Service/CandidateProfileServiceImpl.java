package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CandidateProfileRequestDto;
import com.rakshith.JobApplication.Entity.Candidate;
import com.rakshith.JobApplication.Entity.CandidateProfile;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Repository.CandidateProfileRepository;
import com.rakshith.JobApplication.Repository.CandidateRespository;
import com.rakshith.JobApplication.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final UserRepository userRepository;
    private final CandidateRespository candidateRespository;
    private final CandidateProfileRepository candidateProfileRepository;

    public CandidateProfileServiceImpl(UserRepository userRepository, CandidateRespository candidateRespository, CandidateProfileRepository candidateProfileRepository) {
        this.userRepository = userRepository;
        this.candidateRespository = candidateRespository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    @Transactional
    @Override
    public Boolean addCandidateProfile(CandidateProfileRequestDto candidateProfileRequestDto) {
        // Step 1: Get logged-in user
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        // Step 2: Find User
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Step 3: Find Candidate
        Candidate candidate = user.getCandidate();

        if (candidate == null) {
            throw new IllegalStateException("No Candidate Found.");
        }

        if(!candidateProfileRepository.existsByCandidate(candidate)){
            CandidateProfile candidateProfile = new CandidateProfile();

            candidateProfile.setCandidate(candidate);
            candidateProfile.setLocation(candidateProfileRequestDto.getLocation());
            candidateProfile.setEmail(candidateProfileRequestDto.getEmail());
            candidateProfile.setExperience(candidateProfileRequestDto.getExperience());
            candidateProfile.setFullName(candidateProfileRequestDto.getFullName());
            candidateProfile.setPhoneNo(candidateProfileRequestDto.getPhoneNo());
            candidateProfile.setCurrentDesignation(candidateProfileRequestDto.getCurrentDesignation());
            candidateProfile.setProfessionalSummary(candidateProfileRequestDto.getProfessionalSummary());
            candidateProfile.setSkills(candidateProfileRequestDto.getSkills());

            candidateProfileRepository.save(candidateProfile);
            return true;
        }else{
            return false;
        }
    }
}
