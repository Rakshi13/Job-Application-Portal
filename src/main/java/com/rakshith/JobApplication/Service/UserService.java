package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.LoginRequest;
import com.rakshith.JobApplication.DTO.LoginResponse;
import com.rakshith.JobApplication.DTO.RegisterRequest;
import com.rakshith.JobApplication.DTO.RegisterResponse;

import java.util.List;

public interface UserService {

    //add users
    RegisterResponse register(RegisterRequest registerRequest);

    //get all users
    List<RegisterResponse> fetchAllUsers();

    //Login User
    LoginResponse loginUser(LoginRequest loginRequest);
}
