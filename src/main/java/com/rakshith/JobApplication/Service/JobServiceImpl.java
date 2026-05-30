package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.JobRequest;
import com.rakshith.JobApplication.DTO.JobResponse;
import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Entity.Job;
import com.rakshith.JobApplication.Repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    private JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
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
    public void createJob(JobRequest jobRequest) {
        Job job=new Job();
        updateJobByRequest(jobRequest,job);
        jobRepository.save(job);
    }

    //find Job By Id
    @Override
    public JobResponse findByID(Long id) {
        Job jobData= jobRepository.findById(id).orElse(null);
        return mapToResponse(jobData);
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
        jobResponse.setTitle(jobs.getTitle());
        jobResponse.setLocation(jobs.getLocation());
        jobResponse.setDescription(jobs.getDescription());
        jobResponse.setMaxSalary(jobs.getMaxSalary());
        jobResponse.setMinSalary(jobResponse.getMinSalary());
        if(jobs.getCompany()!=null){
            jobResponse.setCompanyId(jobs.getCompany().getId());
        }
        return jobResponse;
    }

    private void updateJobByRequest(JobRequest jobRequest, Job job) {
        job.setTitle(jobRequest.getTitle());
        job.setLocation(jobRequest.getLocation());
        job.setDescription(jobRequest.getDescription());
        job.setMaxSalary(jobRequest.getMaxSalary());
        job.setMinSalary(jobRequest.getMinSalary());

        if(jobRequest.getCompanyId()!=null){
            Company company=new Company();
            company.setId(jobRequest.getCompanyId());
            job.setCompany(company);
        }
        ;
        jobRepository.save(job);
    }
}
