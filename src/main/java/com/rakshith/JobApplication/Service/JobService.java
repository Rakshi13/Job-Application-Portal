package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.Repository.Job;

import java.util.List;

public interface JobService {
     //Get All Jobs
     List<Job> findAll();

     //Add Job
     void createJob(Job job);

     //Get Job By id
     Job findByID(Long id);

     //Delete Job By Id
     Boolean deleteJobById(Long id);

     //update job based on id
     Boolean updateJobById(Job job, Long id);
}
