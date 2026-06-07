package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.*;
import com.rakshith.JobApplication.Service.UserService;
import com.rakshith.JobApplication.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService,JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil=jwtUtil;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(userService.loginUser(loginRequest),HttpStatus.OK);
    }

    @GetMapping("/token")
    public String generateToken() {
        return jwtUtil.generateToken("rakshith");
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token) {
        boolean valid = jwtUtil.validateToken(token);
        return "Token Valid = " + valid;
    }

    @GetMapping("/username")
    public String extractUsername(@RequestParam String token) {

        return jwtUtil.extractUsername(token);
    }
}
