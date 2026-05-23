package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.Repository.Job;
import com.rakshith.JobApplication.Service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    //Get all jobs
    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> findAll(){
        return ResponseEntity.ok(jobService.findAll());
    }

    //create job
    @PostMapping("/jobs")
    public ResponseEntity<String> addJob(@RequestBody Job job){
        jobService.createJob(job);
        return new ResponseEntity<>("Job added successfully.",HttpStatus.OK);
    }

    //Get Job based on ID
    @GetMapping("/jobs/{id}")
    public ResponseEntity<Job> findJobById(@PathVariable Long id){
        Job job= jobService.findByID(id);
        if(job!=null){
            return new ResponseEntity<>(job,HttpStatus.OK);
        }
       return new ResponseEntity<>(job,HttpStatus.NOT_FOUND);
    }

    //delete Job based on ID
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id){
        Boolean jobFound=jobService.deleteJobById(id);
        if(jobFound){
            return ResponseEntity.ok("Job Deleted Successfully");
        }
        return new ResponseEntity<>("Job Not Found.",HttpStatus.NOT_FOUND);
    }

    //update Job
    @PutMapping("/jobs/{id}")
    public ResponseEntity<String> updateJob(@RequestBody Job job,@PathVariable Long id){
       Boolean jobData= jobService.updateJobById(job,id);
       if(jobData){
           return new ResponseEntity<>("Job Updated Successfully.",HttpStatus.OK);
       }
       return new ResponseEntity<>("Job Not found",HttpStatus.NOT_FOUND);
    }



}
