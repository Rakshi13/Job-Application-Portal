package com.rakshith.JobApplication.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    //Testing Role based Auth
    @GetMapping("/test")
    public String adminTest() {
        return "Welcome Admin! You are authorized to access Admin APIs.";
    }
}
