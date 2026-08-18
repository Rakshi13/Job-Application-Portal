package com.rakshith.JobApplication.DTO;

import lombok.Data;

@Data
public class CompanyResponse {

    private Long id;

    private String name;

    private String email;

    private String website;

    private String description;
}
