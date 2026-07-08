package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.EmployerRegisterRequest;
import com.rakshith.JobApplication.Service.EmployerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    private final EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @GetMapping("/test")
    public String employerTest() {
        return "Welcome Employer! You are authorized to access Employer APIs.";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerEmployer(@Valid @RequestBody EmployerRegisterRequest employerRegisterRequest){
        employerService.createEmployer(employerRegisterRequest);

        return ResponseEntity.ok("Employer Registered Successfully.");

    }
}
