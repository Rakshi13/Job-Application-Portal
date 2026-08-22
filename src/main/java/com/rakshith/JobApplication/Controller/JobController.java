package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.JobRequest;
import com.rakshith.JobApplication.DTO.JobResponse;
import com.rakshith.JobApplication.Service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            summary = "Get Employer Jobs",
            description = "Returns all jobs belonging to the logged-in employer's company"
    )
    @PreAuthorize("hasRole('EMPLOYER')")
    @GetMapping("/jobs")
    public ResponseEntity<List<JobResponse>> findEmployerJobs() {

        return ResponseEntity.ok(jobService.findEmployerJobs());
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

    @Operation(
            summary = "Create a new Job",
            description = "Creates a new Job to the specific company"
    )
    @PostMapping("/jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody JobRequest jobRequest) {

        JobResponse response =
                jobService.createJob(jobRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
