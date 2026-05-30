package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CompanyRequest;
import com.rakshith.JobApplication.DTO.CompanyResponse;
import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    //Add Company
    @Override
    public void addCompanyData(CompanyRequest companyRequest) {
        Company company=new Company();
        updateCompanyDetials(companyRequest,company);
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
        return mapToCompanyResponse(companyResponse);
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
        companyRepository.save(company);
    }

    private CompanyResponse mapToCompanyResponse(Company company) {
        CompanyResponse companyResponse=new CompanyResponse();
        companyResponse.setDescription(company.getDescription());
        companyResponse.setName(company.getName());
        companyResponse.setTotalJobs(company.getJobs().size());
        companyResponse.setTotalReviews(company.getReviews().size());
        return companyResponse;
    }
}
