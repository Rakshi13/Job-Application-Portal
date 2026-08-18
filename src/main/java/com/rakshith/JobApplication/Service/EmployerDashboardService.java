package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CompanyResponse;
import com.rakshith.JobApplication.DTO.EmployerDashboardResponse;
import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Entity.Employer;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EmployerDashboardService {
    private final UserRepository userRepository;

    public EmployerDashboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public EmployerDashboardResponse getDashboardEmployerDetails(){

        //step 1 : get the username from Spring Security
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        //step 2 : from the user find the company name .

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Employer employer=user.getEmployer();

        Company company=employer.getCompany();

        if(company==null){
            return new EmployerDashboardResponse(username,false,null);
        }else{

            CompanyResponse companyResponse =
                    new CompanyResponse(
                            company.getId(),
                            company.getName(),
                            company.getEmail(),
                            company.getWebsite(),
                            company.getDescription()
                    );

            return new EmployerDashboardResponse(
                    username,
                    true,
                    companyResponse
            );
        }
    }
}
