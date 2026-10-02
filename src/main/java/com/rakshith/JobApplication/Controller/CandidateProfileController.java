package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.CandidateProfileRequestDto;
import com.rakshith.JobApplication.DTO.CandidateProfileResponseDto;
import com.rakshith.JobApplication.Service.CandidateProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> addCandidateProfile(@Valid @RequestBody CandidateProfileRequestDto candidateProfileRequestDto) {
        Boolean isAdded = candidateProfileService.addCandidateProfile(candidateProfileRequestDto);

        if (isAdded) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Candidate profile created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Candidate profile already exists.");
        }
    }

    @Operation(
            summary = "Get the Information of Candidate Profile.",
            description = "Get the Candidate Profile Data."
    )
    @GetMapping("/candidate/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<CandidateProfileResponseDto> getCandidateProfile() {
        return ResponseEntity.ok(candidateProfileService.getCandidateprofileData());
    }


    @Operation(
            summary = "Update the candidate profile",
            description = "Update the logged-in candidate's profile data."
    )
    @PutMapping("candidate/profile")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<String> editCandidateProfileData(@Valid @RequestBody CandidateProfileRequestDto candidateProfileRequestDto) {
        candidateProfileService.editCandidateProfile(candidateProfileRequestDto);

        return ResponseEntity.ok("Profile updated successfully.");
    }

}
