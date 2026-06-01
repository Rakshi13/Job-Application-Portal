package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.JobRequest;
import com.rakshith.JobApplication.DTO.JobResponse;
import com.rakshith.JobApplication.Entity.Job;
import com.rakshith.JobApplication.Service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobController {

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    //Get all jobs
    @Operation(
            summary = "Get All Jobs",
            description = "Returns all the jobs available in the system"
    )
    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> findAll(){
        return ResponseEntity.ok(jobService.findAll());
    }

    //create job
    @Operation(
            summary = "Create a new Job",
            description = "Creates a new Job to the specific company"
    )
    @PostMapping("/jobs")
    public ResponseEntity<String> addJob(@Valid @RequestBody JobRequest jobRequest){
        jobService.createJob(jobRequest);
        return new ResponseEntity<>("Job added successfully.",HttpStatus.OK);
    }

    //Get Job based on ID
    @Operation(
            summary = "Get a Job based on the Job Id",
            description = "Returns all the jobs specific to the id"
    )
    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobResponse> findJobById(@PathVariable Long id){
        JobResponse jobResponse= jobService.findByID(id);
        if(jobResponse!=null){
            return new ResponseEntity<>(jobResponse,HttpStatus.OK);
        }
       return new ResponseEntity<>(jobResponse,HttpStatus.NOT_FOUND);
    }

    //delete Job based on ID
    @Operation(
            summary = "Delete a job",
            description = "Deletes the job based on the ID."
    )
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id){
        Boolean jobFound=jobService.deleteJobById(id);
        if(jobFound){
            return ResponseEntity.ok("Job Deleted Successfully");
        }
        return new ResponseEntity<>("Job Not Found.",HttpStatus.NOT_FOUND);
    }

    //update Job
    @Operation(
            summary = "Update the Job based on the Job Id",
            description = "Updates the job based on the Id."
    )
    @PutMapping("/jobs/{id}")
    public ResponseEntity<String> updateJob(@Valid @RequestBody JobRequest jobRequest,@PathVariable Long id){
       Boolean jobData= jobService.updateJobById(jobRequest,id);
       if(jobData){
           return new ResponseEntity<>("Job Updated Successfully.",HttpStatus.OK);
       }
       return new ResponseEntity<>("Job Not found",HttpStatus.NOT_FOUND);
    }
}
