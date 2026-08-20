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

        // Step 1: Get username from Spring Security
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        //step 2 : from the user find the company name .

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 3: Get Employer
        Employer employer=user.getEmployer();

        if (employer == null) {
            throw new RuntimeException("Employer profile not found");
        }

        // Step 4: Get Company
        Company company=employer.getCompany();

        // Step 5: Employer doesn't have a company
        if(company==null){
            return new EmployerDashboardResponse(username,false,null);
        }else{
            // Step 6: Employer has a company
            CompanyResponse companyResponse =
                    new CompanyResponse(
                            company.getId(),
                            company.getName(),
                            company.getEmail(),
                            company.getWebsite(),
                            company.getDescription()
                    );

            return new EmployerDashboardResponse(username, true, companyResponse);
        }
    }
}
