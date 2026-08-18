package com.rakshith.JobApplication.DTO;

import com.rakshith.JobApplication.Entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDashboardResponse {
    private String username;
    private boolean hasCompany;
    private CompanyResponse company;
}
