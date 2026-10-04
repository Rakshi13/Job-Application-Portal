package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CompanyResponse;
import com.rakshith.JobApplication.DTO.EmployerDashboardResponse;
import com.rakshith.JobApplication.DTO.EmployerRegisterRequest;
import com.rakshith.JobApplication.DTO.JobApplicantResponseDto;
import com.rakshith.JobApplication.Entity.*;
import com.rakshith.JobApplication.Enums.Role;
import com.rakshith.JobApplication.Repository.*;
import com.rakshith.JobApplication.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployerService {

    private final PasswordEncoder passwordEncoder;
    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    public EmployerService(PasswordEncoder passwordEncoder, EmployerRepository employerRepository, UserRepository userRepository, JobRepository jobRepository, JobApplicationRepository jobApplicationRepository, CandidateProfileRepository candidateProfileRepository) {
        this.passwordEncoder = passwordEncoder;
        this.employerRepository = employerRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    @Transactional
    public void createEmployer(EmployerRegisterRequest request) {
        // 1. Check username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // 2. Create User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setRole(Role.ROLE_EMPLOYER);
        userRepository.save(user);

        // 3. Create Employer
        Employer employer = new Employer();
        employer.setUser(user);

        // Company is NULL initially
        employer.setCompany(null);
        employerRepository.save(employer);

    }

    public List<JobApplicantResponseDto> getJobApplicants(Long jobId) {

        // 1. Get the logged-in employer
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Employer employer = user.getEmployer();

        if (employer == null) {
            throw new ResourceNotFoundException("Employer not found");
        }

        // 2. Find the requested job
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found"));

        // 3. Verify that the job belongs to this employer
        if (employer.getCompany() == null
                || !job.getCompany().getId()
                .equals(employer.getCompany().getId())) {
            throw new AccessDeniedException(
                    "You are not authorized to view applicants for this job");
        }

        // 4. Fetch applications for this job
        List<JobApplication> applications =
                jobApplicationRepository.findByJob_Id(jobId);

        // 5. Convert applications to response DTOs
        return applications.stream()
                .map(application -> {
                    Candidate candidate = application.getCandidate();
                    CandidateProfile profile = candidateProfileRepository
                            .findByCandidate_Id(candidate.getId())
                            .orElse(null);

                    return new JobApplicantResponseDto(
                            application.getId(),
                            candidate.getId(),
                            profile != null ? profile.getFullName() : null,
                            candidate.getUser().getUsername(),
                            profile != null ? profile.getPhoneNo() : null,
                            profile != null ? profile.getLocation() : null,
                            profile != null ? profile.getSkills() : null,
                            profile != null ? profile.getResumeFileName() : null,
                            application.getAppliedDate()
                    );
                })
                .toList();
    }
}
