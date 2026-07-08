package com.rakshith.JobApplication.Repository;

import com.rakshith.JobApplication.Entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends JpaRepository<Employer,Long> {
}
