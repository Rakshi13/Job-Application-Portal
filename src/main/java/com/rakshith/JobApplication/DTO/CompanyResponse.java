package com.rakshith.JobApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {

    private Long id;

    private String name;

    private String email;

    private String website;

    private String description;
}
