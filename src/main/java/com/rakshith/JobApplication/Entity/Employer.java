package com.rakshith.JobApplication.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String companyEmail;
    private String companyWebsite;
    private String HrName;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
