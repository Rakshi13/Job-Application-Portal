package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.RegisterRequest;
import com.rakshith.JobApplication.DTO.RegisterResponse;
import com.rakshith.JobApplication.DTO.ReviewResponse;
import com.rakshith.JobApplication.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> userRegister(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = userService.register(registerRequest);
        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/register")
    public ResponseEntity<List<RegisterResponse>> getAllUsers() {
        List<RegisterResponse> registerResponses = userService.fetchAllUsers();
        if (!registerResponses.isEmpty()) {
            return new ResponseEntity<>(registerResponses, HttpStatus.OK);
        }
        return new ResponseEntity<>(registerResponses, HttpStatus.NOT_FOUND);
    }
}
