package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CompanyResponse;
import com.rakshith.JobApplication.DTO.EmployerDashboardResponse;
import com.rakshith.JobApplication.DTO.EmployerRegisterRequest;
import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Entity.Employer;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Enums.Role;
import com.rakshith.JobApplication.Repository.EmployerRepository;
import com.rakshith.JobApplication.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void createEmployer(EmployerRegisterRequest employerRegisterRequest) {

        if (userRepository.existsByUsername(
                employerRegisterRequest.getUsername())) {

            throw new RuntimeException("Username already exists");
        }

        // Create User
        User user = new User();

        user.setUsername(employerRegisterRequest.getUsername());
        user.setPassword(
                passwordEncoder.encode(
                        employerRegisterRequest.getPassword()
                )
        );
        user.setRole(Role.ROLE_EMPLOYER);

        userRepository.save(user);


        // Create Employer
        Employer employer = new Employer();

        employer.setHrName(employerRegisterRequest.getHrName());
        employer.setUser(user);

        employerRepository.save(employer);
    }
}
