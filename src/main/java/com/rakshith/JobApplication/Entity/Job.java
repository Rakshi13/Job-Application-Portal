package com.rakshith.JobApplication.Entity;

import jakarta.persistence.*;


@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Long maxSalary;
    private Long minSalary;
    private String location;

    @ManyToOne
    private Company company;

    //default constructor
    public Job(){

    }

    public Job(Long id, String location, Long minSalary, Long maxSalary, String title, String description,Company company) {
        this.id = id;
        this.location = location;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.title = title;
        this.description = description;
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Long maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Long getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Long minSalary) {
        this.minSalary = minSalary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", maxSalary=" + maxSalary +
                ", minSalary=" + minSalary +
                ", location='" + location + '\'' +
                '}';
    }
}
