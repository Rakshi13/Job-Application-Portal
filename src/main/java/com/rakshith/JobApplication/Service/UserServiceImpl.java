package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.LoginRequest;
import com.rakshith.JobApplication.DTO.LoginResponse;
import com.rakshith.JobApplication.DTO.RegisterRequest;
import com.rakshith.JobApplication.DTO.RegisterResponse;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Repository.UserRepository;
import com.rakshith.JobApplication.exception.InvalidCredentialsException;
import com.rakshith.JobApplication.exception.ResourceNotFoundException;
import com.rakshith.JobApplication.exception.UserAlreadyExistsException;
import com.rakshith.JobApplication.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        //check the duplicate users exists or not. 409 conflict.
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());

        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getRole());
        User savedUser = userRepository.save(user);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(savedUser.getId());
        registerResponse.setUsername(savedUser.getUsername());
        registerResponse.setMessage("User Register Successfully.");
        return registerResponse;
    }

    @Override
    public List<RegisterResponse> fetchAllUsers() {
        return userRepository.findAll().
                stream().map(this::mapToRegisterResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if(user==null){
            throw new InvalidCredentialsException("User not found");
        }

        //passwordEncoder.matches(rawPassword, encodedPassword)
        //rawPassword = what user enters during login
        //encodedPassword = what is stored in DB
        if (!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        String token =
                jwtUtil.generateToken(
                        user.getUsername(),
                        user.getRole().name()
                );

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setMessage("Login successful");

        return response;
    }

    public RegisterResponse mapToRegisterResponse(User user) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUsername(user.getUsername());
        registerResponse.setId(user.getId());
        registerResponse.setMessage("User role is ->" + user.getRole());
        return registerResponse;
    }
}
