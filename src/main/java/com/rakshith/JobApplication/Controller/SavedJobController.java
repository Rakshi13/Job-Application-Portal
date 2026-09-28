package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.Service.SaveJobService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SavedJobController {
    private SaveJobService saveJobService;

    public SavedJobController(SaveJobService saveJobService){
        this.saveJobService=saveJobService;
    }

    @Operation(
            summary = "Save job for Candidate.",
            description = "Saving the job for candidate."
    )
    @PreAuthorize("hasRole('CANDIDATE')")
    @PostMapping("/jobs/{jobId}/save")
    public ResponseEntity<String> saveJob(@PathVariable Long jobId){
        Boolean savedJob=saveJobService.saveCurrentJob(jobId);

        if(savedJob){
            return ResponseEntity.ok("Job Saved Succesfully");
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Job is already saved");
        }
    }
}
