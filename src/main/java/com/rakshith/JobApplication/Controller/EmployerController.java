package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.EmployerRegisterRequest;
import com.rakshith.JobApplication.Service.EmployerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String,String>> registerEmployer(@Valid @RequestBody EmployerRegisterRequest employerRegisterRequest){
        employerService.createEmployer(employerRegisterRequest);

        Map<String,String>response=new HashMap<>();
        response.put("message","Employer Registered Successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}
