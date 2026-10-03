package com.rakshith.JobApplication.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "candidate_profile")
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String location;

    @Column(length = 2000)
    private String skills;

    private String email;

    private Integer experience;

    private String phoneNo;

    private String currentDesignation;

    @Column(length = 1000)
    private String professionalSummary;

    //Resume metadata
    @Column(name = "resume_content_type", length = 100)
    private String resumeContentType;

    @Column(name = "resume_file_name", length = 255)
    private String resumeFileName;

    @Column(name = "resume_file_size")
    private Long resumeFileSize;

    //path or key to locate the actual resume.
    @Column(name = "resume_storage_key", unique = true, length = 255)
    private String resumeStorageKey;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(
            name="candidate_id",
            nullable = false,
            unique = true
    )
    private Candidate candidate;
}
