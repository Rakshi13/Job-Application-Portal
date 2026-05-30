package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.JobRequest;
import com.rakshith.JobApplication.DTO.JobResponse;
import com.rakshith.JobApplication.Entity.Job;

import java.util.List;

public interface JobService {

     //Get All Jobs
     List<JobResponse> findAll();

     //Add Job
     void createJob(JobRequest jobRequest);

     //Get Job By id
     JobResponse findByID(Long id);

     //Delete Job By Id
     Boolean deleteJobById(Long id);

     //update job based on id
     Boolean updateJobById(JobRequest jobRequest, Long id);
}
