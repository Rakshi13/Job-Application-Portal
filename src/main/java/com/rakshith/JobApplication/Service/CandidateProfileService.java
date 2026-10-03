package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CandidateProfileRequestDto;
import com.rakshith.JobApplication.DTO.CandidateProfileResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CandidateProfileService {

    //create candidate profile.
    Boolean addCandidateProfile(CandidateProfileRequestDto candidateProfileRequestDto);

    //Get the details of candidate profile.
    CandidateProfileResponseDto getCandidateprofileData();

    //Edit the candidate profile.
    void editCandidateProfile(CandidateProfileRequestDto candidateProfileRequestDto);

    String uploadResume(MultipartFile file) throws IOException;
}
