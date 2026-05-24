package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    //Add Company
    @Override
    public Company addCompanyData(Company company) {
        return companyRepository.save(company);
    }

    //fetch all companies
    @Override
    public List<Company> fetchAllCompanies() {
        return companyRepository.findAll();
    }

    //fetch Company by ID
    @Override
    public Company fetchCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    //modify Company By ID
    @Override
    public Boolean modifyCompanyById(Long id, Company company) {
        Optional<Company> companyDetails = companyRepository.findById(id);

        if (companyDetails.isPresent()) {
            Company companyData = companyDetails.get();
            companyData.setName(company.getName());
            companyData.setDescription(company.getDescription());
            companyData.setJobs(company.getJobs());
            companyRepository.save(companyData);
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
}
