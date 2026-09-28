package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.Entity.Candidate;
import com.rakshith.JobApplication.Entity.Job;
import com.rakshith.JobApplication.Entity.SavedJob;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Repository.JobRepository;
import com.rakshith.JobApplication.Repository.SavedJobRepository;
import com.rakshith.JobApplication.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SaveJobService {
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final SavedJobRepository savedJobRepository;

    public SaveJobService(UserRepository userRepository, JobRepository jobRepository, SavedJobRepository savedJobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.savedJobRepository = savedJobRepository;
    }

    @Transactional
    public Boolean saveCurrentJob(Long jobId){
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

        Job job=jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job Not Found"));

        if(!savedJobRepository.existsByCandidateAndJob(candidate, job)){
            SavedJob savedJob=new SavedJob();
            savedJob.setJob(job);
            savedJob.setCandidate(candidate);
            savedJobRepository.save(savedJob);

            return true;
        }else {
            return false;
        }
    }
}
