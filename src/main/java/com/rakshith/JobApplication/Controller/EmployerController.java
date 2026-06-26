package com.rakshith.JobApplication.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    @GetMapping("/test")
    public String employerTest() {
        return "Welcome Employer! You are authorized to access Employer APIs.";
    }
}
