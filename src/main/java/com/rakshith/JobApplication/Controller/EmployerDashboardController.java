package com.rakshith.JobApplication.Controller;

import com.rakshith.JobApplication.DTO.EmployerDashboardResponse;
import com.rakshith.JobApplication.Service.EmployerDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employer")
public class EmployerDashboardController {

    private final EmployerDashboardService employerDashboardService;

    public EmployerDashboardController(
            EmployerDashboardService employerDashboardService) {
        this.employerDashboardService = employerDashboardService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<EmployerDashboardResponse> getDashboard() {

        EmployerDashboardResponse response =
                employerDashboardService.getDashboardEmployerDetails();

        return ResponseEntity.ok(response);
    }
}
