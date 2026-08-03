package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.CandidateRegisterRequest;
import com.rakshith.JobApplication.Service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/candidate")
public class CandidateController {
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService){
        this.candidateService = candidateService;
    }


    @GetMapping("/test")
    public String candidateTest() {
        return "Welcome Candidate! You are authorized to access Candidate APIs.";
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerCandidate(@Valid @RequestBody CandidateRegisterRequest candidateRegisterRequest){
        candidateService.createCandidates(candidateRegisterRequest);

        Map<String,String>response=new HashMap<>();
        response.put("message","Candidate Registered Successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}