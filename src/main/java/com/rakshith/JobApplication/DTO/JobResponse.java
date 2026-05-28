package com.rakshith.JobApplication.DTO;


import java.util.Objects;

public class JobResponse {

    private String title;
    private String description;
    private Long maxSalary;
    private Long minSalary;
    private String location;

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
        return "JobResponse{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", maxSalary=" + maxSalary +
                ", minSalary=" + minSalary +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobResponse that = (JobResponse) o;
        return Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(maxSalary, that.maxSalary) && Objects.equals(minSalary, that.minSalary) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, maxSalary, minSalary, location);
    }
}
