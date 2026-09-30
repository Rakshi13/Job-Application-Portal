package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.CandidateProfileRequestDto;
import com.rakshith.JobApplication.Service.CandidateProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    public CandidateProfileController(CandidateProfileService candidateProfileService) {
        this.candidateProfileService = candidateProfileService;
    }

    @Operation(
            summary = "Add Profile for Candidate.",
            description = "Saving Candidate Profile Information."
    )
    @PostMapping("/candidate/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<String> addCandidateProfile(@Valid @RequestBody CandidateProfileRequestDto candidateProfileRequestDto){
        Boolean isAdded=candidateProfileService.addCandidateProfile(candidateProfileRequestDto);

        if(isAdded){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Candidate profile created successfully");
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Candidate profile already exists.");
        }
    }
}
