package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CompanyRequest;
import com.rakshith.JobApplication.DTO.CompanyResponse;
import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Entity.Employer;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Repository.CompanyRepository;
import com.rakshith.JobApplication.Repository.EmployerRepository;
import com.rakshith.JobApplication.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private EmployerRepository employerRepository;

    public CompanyServiceImpl(
            CompanyRepository companyRepository,
            UserRepository userRepository,
            EmployerRepository employerRepository) {

        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.employerRepository = employerRepository;
    }

    //Add Company
    @Override
    @Transactional
    public void addCompanyData(CompanyRequest companyRequest) {
        // 1. Get logged-in user
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        // 2. Find User
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // 3. Get Employer
        Employer employer = user.getEmployer();

        if (employer == null) {
            throw new RuntimeException("Employer not found");
        }

        // 4. Check if employer already has a company
        if (employer.getCompany() != null) {
            throw new RuntimeException("Employer already has a company");
        }

        // 5. Create Company
        Company company = new Company();

        company.setName(companyRequest.getName());
        company.setDescription(companyRequest.getDescription());
        company.setWebsite(companyRequest.getWebsite());
        company.setEmail(companyRequest.getEmail());

        // 6. IMPORTANT - connect company to employer
        employer.setCompany(company);

        // 7. Save company
        companyRepository.save(company);

        // 8. Save employer
        employerRepository.save(employer);
    }

    //fetch all companies
    @Override
    public List<CompanyResponse> fetchAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::mapToCompanyResponse)
                .collect(Collectors.toList());
    }

    //fetch Company by ID
    @Override
    public CompanyResponse fetchCompanyById(Long id) {
        Company companyResponse= companyRepository.findById(id).orElse(null);
        if(companyResponse!=null){
            return mapToCompanyResponse(companyResponse);
        }
        return null;
    }

    //modify Company By ID
    @Override
    public Boolean modifyCompanyById(Long id, CompanyRequest companyRequest) {
        Optional<Company> companyDetails = companyRepository.findById(id);

        if (companyDetails.isPresent()) {
            Company companyData = companyDetails.get();
            updateCompanyDetials(companyRequest,companyData);;
            return true;
        }
        return false;
    }

    //Delete Company by ID
    @Override
    public Boolean removeCompanyById(Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    private void updateCompanyDetials(CompanyRequest companyRequest, Company company) {
        company.setName(companyRequest.getName());
        company.setDescription(companyRequest.getDescription());
        company.setWebsite(companyRequest.getWebsite());
        company.setEmail(companyRequest.getEmail());
        companyRepository.save(company);
    }

    private CompanyResponse mapToCompanyResponse(Company company) {
        CompanyResponse companyResponse=new CompanyResponse();
        companyResponse.setId(company.getId());
        companyResponse.setDescription(company.getDescription());
        companyResponse.setName(company.getName());
        companyResponse.setWebsite(company.getWebsite());
        return companyResponse;
    }
}
