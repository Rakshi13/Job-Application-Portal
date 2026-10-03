package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CandidateProfileRequestDto;
import com.rakshith.JobApplication.DTO.CandidateProfileResponseDto;
import com.rakshith.JobApplication.Entity.Candidate;
import com.rakshith.JobApplication.Entity.CandidateProfile;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Repository.CandidateProfileRepository;
import com.rakshith.JobApplication.Repository.CandidateRespository;
import com.rakshith.JobApplication.Repository.UserRepository;
import com.rakshith.JobApplication.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final UserRepository userRepository;
    private final CandidateRespository candidateRespository;
    private final CandidateProfileRepository candidateProfileRepository;

    @Value("${app.resume.upload-dir}")
    private String resumeUploadDir;

    public CandidateProfileServiceImpl(UserRepository userRepository, CandidateRespository candidateRespository, CandidateProfileRepository candidateProfileRepository) {
        this.userRepository = userRepository;
        this.candidateRespository = candidateRespository;
        this.candidateProfileRepository = candidateProfileRepository;
    }

    @Transactional
    @Override
    public Boolean addCandidateProfile(CandidateProfileRequestDto candidateProfileRequestDto) {
        // Step 1: Get logged-in user
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        // Step 2: Find User
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Step 3: Find Candidate
        Candidate candidate = user.getCandidate();

        if (candidate == null) {
            throw new IllegalStateException("No Candidate Found.");
        }

        if(!candidateProfileRepository.existsByCandidate(candidate)){
            CandidateProfile candidateProfile = new CandidateProfile();

            candidateProfile.setCandidate(candidate);
            candidateProfile.setLocation(candidateProfileRequestDto.getLocation());
            candidateProfile.setEmail(candidateProfileRequestDto.getEmail());
            candidateProfile.setExperience(candidateProfileRequestDto.getExperience());
            candidateProfile.setFullName(candidateProfileRequestDto.getFullName());
            candidateProfile.setPhoneNo(candidateProfileRequestDto.getPhoneNo());
            candidateProfile.setCurrentDesignation(candidateProfileRequestDto.getCurrentDesignation());
            candidateProfile.setProfessionalSummary(candidateProfileRequestDto.getProfessionalSummary());
            candidateProfile.setSkills(candidateProfileRequestDto.getSkills());

            candidateProfileRepository.save(candidateProfile);
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional
    public CandidateProfileResponseDto getCandidateprofileData() {
        // Step 1: Get logged-in user
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        // Step 2: Find User
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Step 3: Find Candidate
        Candidate candidate = user.getCandidate();

        if (candidate == null) {
            throw new IllegalStateException("No Candidate Found.");
        }

        CandidateProfile candidateProfile= candidateProfileRepository.findByCandidate_Id(candidate.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Candidate profile not found"
                        ));

        return mapCandidateResponse(candidateProfile);
    }

    @Override
    @Transactional
    public void editCandidateProfile(CandidateProfileRequestDto profileRequestDto) {
        // Step 1: Get logged-in user
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username = authentication.getName();

        // Step 2: Find User
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Step 3: Find Candidate
        Candidate candidate = user.getCandidate();

        if (candidate == null) {
            throw new IllegalStateException("No Candidate Found.");
        }

        // Step 4: Find existing Candidate Profile
        CandidateProfile profile = candidateProfileRepository
                .findByCandidate_Id(candidate.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Candidate profile not found"));

        // Step 5: Update existing profile fields
        profile.setFullName(profileRequestDto.getFullName());
        profile.setEmail(profileRequestDto.getEmail());
        profile.setPhoneNo(profileRequestDto.getPhoneNo());
        profile.setLocation(profileRequestDto.getLocation());
        profile.setSkills(profileRequestDto.getSkills());
        profile.setExperience(profileRequestDto.getExperience());
        profile.setCurrentDesignation(
                profileRequestDto.getCurrentDesignation());
        profile.setProfessionalSummary(
                profileRequestDto.getProfessionalSummary());

        // Step 6: Save updated profile
        candidateProfileRepository.save(profile);
    }

    @Override
    @Transactional
    public String uploadResume(MultipartFile file) throws IOException {

        // Step 1: Validate the uploaded file
        validateResume(file);

        // Step 2: Get the logged-in user's username
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        // Step 3: Find the user in the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new IllegalStateException("User not found."));

        // Step 4: Get the candidate associated with this user
        Candidate candidate = user.getCandidate();

        if (candidate == null) {
            throw new IllegalStateException("Candidate not found.");
        }

        // Step 5: Find the candidate's profile
        CandidateProfile profile =
                candidateProfileRepository
                        .findByCandidate_Id(candidate.getId())
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Candidate profile not found. Please create your profile first."
                                ));

        // Step 6: Get the original file name and extension
        String originalFileName =
                StringUtils.cleanPath(file.getOriginalFilename());

        String extension = getFileExtension(originalFileName);

        // Step 7: Generate a unique storage key
        String storageKey = UUID.randomUUID() + "." + extension;

        // Step 8: Create the upload directory if it doesn't exist
        Path uploadPath = Paths.get(resumeUploadDir)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(uploadPath);

        // Step 9: Create the destination path
        Path targetPath = uploadPath.resolve(storageKey).normalize();

        if (!targetPath.startsWith(uploadPath)) {
            throw new IllegalArgumentException("Invalid file path.");
        }

        // Keep the previous key in case this is a replacement
        String oldStorageKey = profile.getResumeStorageKey();

        // Step 10: Save the file to the storage directory
        Files.copy(
                file.getInputStream(),
                targetPath,
                StandardCopyOption.REPLACE_EXISTING
        );

        try {
            // Step 11: Update the resume metadata in the profile
            profile.setResumeFileName(originalFileName);
            profile.setResumeStorageKey(storageKey);
            profile.setResumeContentType(file.getContentType());
            profile.setResumeFileSize(file.getSize());

            // Step 12: Save the updated profile
            candidateProfileRepository.save(profile);

        } catch (RuntimeException exception) {
            // Remove the newly uploaded file if saving metadata fails
            Files.deleteIfExists(targetPath);
            throw exception;
        }

        // Step 13: Delete the previous file after replacing it
        if (oldStorageKey != null && !oldStorageKey.isBlank()) {
            Path oldPath = uploadPath.resolve(oldStorageKey).normalize();

            if (oldPath.startsWith(uploadPath)) {
                Files.deleteIfExists(oldPath);
            }
        }

        // Step 14: Return success message
        return "Resume uploaded successfully.";
    }

    // Validate file before saving
    private void validateResume(MultipartFile file) throws IOException{

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select a resume file."
            );
        }

        // Maximum allowed file size: 5 MB
        long maxFileSize = 5 * 1024 * 1024;

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException(
                    "Resume size must not exceed 5 MB."
            );
        }

        String originalFileName =
                StringUtils.cleanPath(file.getOriginalFilename());

        String extension = getFileExtension(originalFileName);

        if (!extension.equals("pdf")
                && !extension.equals("doc")
                && !extension.equals("docx")) {
            throw new IllegalArgumentException(
                    "Only PDF, DOC and DOCX files are allowed."
            );
        }
    }

    // Extract file extension
    private String getFileExtension(String fileName) {

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException(
                    "File name is missing."
            );
        }

        int lastDot = fileName.lastIndexOf('.');

        if (lastDot < 0 || lastDot == fileName.length() - 1) {
            throw new IllegalArgumentException(
                    "File must have a valid extension."
            );
        }

        return fileName.substring(lastDot + 1)
                .toLowerCase(Locale.ROOT);
    }

    public CandidateProfileResponseDto mapCandidateResponse(CandidateProfile profile){
        CandidateProfileResponseDto responseDto=new CandidateProfileResponseDto();
        responseDto.setEmail(profile.getEmail());
        responseDto.setLocation(profile.getLocation());
        responseDto.setExperience(profile.getExperience());
        responseDto.setSkills(profile.getSkills());
        responseDto.setPhoneNo(profile.getPhoneNo());
        responseDto.setCurrentDesignation(profile.getCurrentDesignation());
        responseDto.setProfessionalSummary(profile.getProfessionalSummary());
        responseDto.setFullName(profile.getFullName());

        return responseDto;

    }
}
