package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.Entity.Job;
import com.rakshith.JobApplication.Repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobServiceImpl implements JobService{
    JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    //Search All Job
    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    //create Job
    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    //find Job By Id
    @Override
    public Job findByID(Long id){
        return jobRepository.findById(id).orElse(null);
    }

    //delete job by id
    @Override
    public Boolean deleteJobById(Long id) {
       if(jobRepository.existsById(id)){
           jobRepository.deleteById(id);
           return true;
       }
       return false;
    }

    //update job
    @Override
    public Boolean updateJobById(Job job, Long id) {

        Optional<Job>optionalJob=jobRepository.findById(id);

        if(optionalJob.isPresent()){
            Job jobData=optionalJob.get();
            jobData.setLocation(job.getLocation());
            jobData.setDescription(job.getDescription());
            jobData.setTitle(job.getTitle());
            jobData.setMinSalary(job.getMinSalary());
            jobData.setMaxSalary(job.getMaxSalary());
            jobRepository.save(jobData);
            return true;
        }
        return false;
    }
}
