package com.rakshith.JobApplication.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @GetMapping("/test")
    public String candidateTest() {
        return "Welcome Candidate! You are authorized to access Candidate APIs.";
    }

}
