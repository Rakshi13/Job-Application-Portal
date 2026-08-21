package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.JobRequest;
import com.rakshith.JobApplication.DTO.JobResponse;
import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Entity.Employer;
import com.rakshith.JobApplication.Entity.Job;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Repository.CompanyRepository;
import com.rakshith.JobApplication.Repository.JobRepository;
import com.rakshith.JobApplication.Repository.UserRepository;
import com.rakshith.JobApplication.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobServiceImpl(
            JobRepository jobRepository,
            UserRepository userRepository) {

        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }


    //Get All Jobs
    @Override
    public List<JobResponse> findAll() {
        return jobRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //create Job
    @Override
    @Transactional
    public JobResponse createJob(JobRequest jobRequest) {
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

        // Step 3: Find Employer
        Employer employer = user.getEmployer();

        if (employer == null) {
            throw new RuntimeException("Employer profile not found");
        }

        // Step 4: Find Company
        Company company = employer.getCompany();

        if (company == null) {
            throw new RuntimeException(
                    "Please create a company before posting a job");
        }

        // Step 5: Create Job
        Job job = new Job();

        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setMinSalary(jobRequest.getMinSalary());
        job.setMaxSalary(jobRequest.getMaxSalary());
        job.setLocation(jobRequest.getLocation());

        // VERY IMPORTANT
        job.setCompany(company);

        // Step 6: Save Job
        Job savedJob = jobRepository.save(job);

        // Step 7: Convert to response
        return mapToJobResponse(savedJob);
    }

    private JobResponse mapToJobResponse(Job job) {
        JobResponse response = new JobResponse();

        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setDescription(job.getDescription());
        response.setMinSalary(job.getMinSalary());
        response.setMaxSalary(job.getMaxSalary());
        response.setLocation(job.getLocation());

        response.setCompanyId(job.getCompany().getId());
        response.setCompanyName(job.getCompany().getName());

        return response;
    }

    //find Job By Id
    @Override
    public JobResponse findByID(Long id) {
        Job jobData= jobRepository.findById(id).orElse(null);
        if(jobData!=null){
            return mapToResponse(jobData);
        }
        return null;
    }

    //delete job by id
    @Override
    public Boolean deleteJobById(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //update job
    @Override
    public Boolean updateJobById(JobRequest jobRequest, Long id) {

        Optional<Job> optionalJob = jobRepository.findById(id);

        if (optionalJob.isPresent()) {
            Job jobData = optionalJob.get();
            updateJobByRequest(jobRequest,jobData);
            return true;
        }
        return false;
    }

    private JobResponse mapToResponse(Job jobs) {
        JobResponse jobResponse=new JobResponse();
        jobResponse.setId(jobs.getId());
        jobResponse.setTitle(jobs.getTitle());
        jobResponse.setLocation(jobs.getLocation());
        jobResponse.setDescription(jobs.getDescription());
        jobResponse.setMaxSalary(jobs.getMaxSalary());
        jobResponse.setMinSalary(jobs.getMinSalary());
        if(jobs.getCompany()!=null){
            jobResponse.setCompanyId(jobs.getCompany().getId());
            jobResponse.setCompanyName(jobs.getCompany().getName());
        }
        return jobResponse;
    }

    private void updateJobByRequest(JobRequest jobRequest, Job job) {
        job.setTitle(jobRequest.getTitle());
        job.setLocation(jobRequest.getLocation());
        job.setDescription(jobRequest.getDescription());
        job.setMaxSalary(jobRequest.getMaxSalary());
        job.setMinSalary(jobRequest.getMinSalary());

        jobRepository.save(job);
    }
}
