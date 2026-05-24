package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.Entity.Company;

import java.util.List;

public interface CompanyService {

    //add Company
    Company addCompanyData(Company company);

    //Get list of companies
    List<Company> fetchAllCompanies();

    //Get Company By ID
    Company fetchCompanyById(Long id);

    //Update Company By ID
    Boolean modifyCompanyById(Long id, Company company);

    //Delete Company By ID
    Boolean removeCompanyById(Long id);
}
