package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CompanyRequest;
import com.rakshith.JobApplication.DTO.CompanyResponse;
import com.rakshith.JobApplication.Entity.Company;

import java.util.List;

public interface CompanyService {

    //add Company
    void addCompanyData(CompanyRequest companyRequest);

    //Get list of companies
    List<CompanyResponse> fetchAllCompanies();

    //Get Company By ID
    CompanyResponse fetchCompanyById(Long id);

    //Update Company By ID
    Boolean modifyCompanyById(Long id, CompanyRequest companyRequest);

    //Delete Company By ID
    Boolean removeCompanyById(Long id);
}
