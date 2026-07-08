package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.CandidateRegisterRequest;
import com.rakshith.JobApplication.Service.CandidaterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    private final CandidaterService candidaterService;

    public CandidateController(CandidaterService candidaterService){
        this.candidaterService = candidaterService;
    }


    @GetMapping("/test")
    public String candidateTest() {
        return "Welcome Candidate! You are authorized to access Candidate APIs.";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCandidate(@Valid @RequestBody CandidateRegisterRequest candidateRegisterRequest){
        candidaterService.createCandidates(candidateRegisterRequest);

        return ResponseEntity.ok("Candidate Registered Successfully.");
    }

}
