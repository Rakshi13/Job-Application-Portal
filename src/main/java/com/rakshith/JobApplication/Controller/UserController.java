package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.LoginRequest;
import com.rakshith.JobApplication.DTO.LoginResponse;
import com.rakshith.JobApplication.DTO.RegisterRequest;
import com.rakshith.JobApplication.DTO.RegisterResponse;
import com.rakshith.JobApplication.Service.UserService;
import com.rakshith.JobApplication.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Register User
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest) {

        RegisterResponse response = userService.register(registerRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get All Users
    @GetMapping("/register")
    public ResponseEntity<List<RegisterResponse>> getAllUsers() {

        List<RegisterResponse> users = userService.fetchAllUsers();

        return ResponseEntity.ok(users);
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody LoginRequest loginRequest) {

        LoginResponse response = userService.loginUser(loginRequest);

        return ResponseEntity.ok(response);
    }

    // Current Logged-in User
    @GetMapping("/whoami")
    public ResponseEntity<String> whoAmI() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        String authorities = authentication
                .getAuthorities()
                .toString();

        String response =
                "Username : " + username +
                        "\nAuthorities : " + authorities;

        return ResponseEntity.ok(response);
    }

    /*
    ------------------------------------------------------------------
    Temporary JWT APIs (Use only for learning/testing)
    Remove these after Role-Based Authentication is completed.
    ------------------------------------------------------------------

    @GetMapping("/token")
    public String generateToken() {
        return jwtUtil.generateToken("rakshith", "ROLE_ADMIN");
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token) {
        return "Token Valid = " + jwtUtil.validateToken(token);
    }

    @GetMapping("/username")
    public String extractUsername(@RequestParam String token) {
        return jwtUtil.extractUsername(token);
    }

    */
}