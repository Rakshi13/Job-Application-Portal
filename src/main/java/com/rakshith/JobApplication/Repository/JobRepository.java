package com.rakshith.JobApplication.Repository;

import com.rakshith.JobApplication.Entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    //Get company by ID
    List<Job> findByCompanyId(Long companyId);
}
