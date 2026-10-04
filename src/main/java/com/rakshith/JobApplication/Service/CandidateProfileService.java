package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CandidateProfileRequestDto;
import com.rakshith.JobApplication.DTO.CandidateProfileResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface CandidateProfileService {

    Boolean addCandidateProfile(CandidateProfileRequestDto candidateProfileRequestDto);

    CandidateProfileResponseDto getCandidateprofileData();

    void editCandidateProfile(CandidateProfileRequestDto candidateProfileRequestDto);

    String uploadResume(MultipartFile file) throws IOException;

    ResumeDownload downloadResume() throws IOException;

    record ResumeDownload(
            Resource resource,
            String fileName,
            String contentType
    ) {}
}
