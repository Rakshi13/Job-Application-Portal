package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.EmployerRegisterRequest;
import com.rakshith.JobApplication.Entity.Employer;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Enums.Role;
import com.rakshith.JobApplication.Repository.EmployerRepository;
import com.rakshith.JobApplication.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployerService {

    private final PasswordEncoder passwordEncoder;
    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;

    public EmployerService(PasswordEncoder passwordEncoder, EmployerRepository employerRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.employerRepository = employerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createEmployer(EmployerRegisterRequest employerRegisterRequest){
        if(userRepository.existsByUsername(employerRegisterRequest.getUsername())){
            throw  new RuntimeException("Username already exists");
        }

        User user=new User();
        user.setUsername(employerRegisterRequest.getUsername());
        user.setPassword(employerRegisterRequest.getPassword());
        user.setRole(Role.ROLE_EMPLOYER);

        userRepository.save(user);

        Employer employer=new Employer();
        employer.setCompanyName(employerRegisterRequest.getCompanyName());
        employer.setCompanyEmail(employerRegisterRequest.getCompanyEmail());
        employer.setCompanyWebsite(employerRegisterRequest.getCompanyWebsite());
        employer.setHrName(employerRegisterRequest.getHrName());
        employer.setUser(user);

        employerRepository.save(employer);

    }
}
