package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.RegisterRequest;
import com.rakshith.JobApplication.DTO.RegisterResponse;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Repository.UserRepository;
import com.rakshith.JobApplication.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        //check the duplicate users exists or not. 409 conflict.
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
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

    public RegisterResponse mapToRegisterResponse(User user) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUsername(user.getUsername());
        registerResponse.setId(user.getId());
        registerResponse.setMessage("User role is ->" + user.getRole());
        return registerResponse;
    }
}
