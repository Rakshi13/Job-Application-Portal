package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.CompanyRequest;
import com.rakshith.JobApplication.DTO.CompanyResponse;
import com.rakshith.JobApplication.Entity.Company;
import com.rakshith.JobApplication.Service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    //add a company
    @PostMapping
    public ResponseEntity<String> addCompany(@RequestBody CompanyRequest companyRequest) {
        return new ResponseEntity<>("Company added successfully", HttpStatus.OK);
    }

    //Get all companies
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        return new ResponseEntity<>(companyService.fetchAllCompanies(), HttpStatus.OK);
    }

    //Get Company based on ID
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        CompanyResponse company = companyService.fetchCompanyById(id);
        if (company != null) {
            return ResponseEntity.ok(company);
        } else {
            return new ResponseEntity<>(company, HttpStatus.NOT_FOUND);
        }
    }

    //update company based on ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompanyById(@PathVariable Long id,@RequestBody CompanyRequest companyRequest){
        Boolean companyFound=companyService.modifyCompanyById(id,companyRequest);
        if(companyFound){
            return new ResponseEntity<>("Company Updated Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Company not found.",HttpStatus.NOT_FOUND);
    }

    //Delete company By ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id){
        Boolean companyFound=companyService.removeCompanyById(id);
        if(companyFound){
            return new ResponseEntity<>("Deleted company Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("No Company Found.",HttpStatus.NOT_FOUND);
    }
}
