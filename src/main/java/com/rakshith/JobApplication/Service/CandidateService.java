package com.rakshith.JobApplication.Service;

import com.rakshith.JobApplication.DTO.CandidateRegisterRequest;
import com.rakshith.JobApplication.Entity.Candidate;
import com.rakshith.JobApplication.Entity.User;
import com.rakshith.JobApplication.Enums.Role;
import com.rakshith.JobApplication.Repository.CandidateRespository;
import com.rakshith.JobApplication.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CandidaterService {
    private CandidateRespository candidateRespository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public CandidaterService(CandidateRespository candidateRespository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.candidateRespository = candidateRespository;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Transactional
    public void createCandidates(CandidateRegisterRequest candidateRegisterRequest){

        if(userRepository.existsByUsername(candidateRegisterRequest.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        //saving the user.
        User user=new User();
        user.setUsername(candidateRegisterRequest.getUsername());
        user.setPassword(passwordEncoder.encode(candidateRegisterRequest.getPassword()));
        user.setRole(Role.ROLE_CANDIDATE);

        userRepository.save(user);

        //saving the candidate.
        Candidate candidate=new Candidate();
        candidate.setFirstName(candidateRegisterRequest.getFirstName());
        candidate.setLastName(candidateRegisterRequest.getLastName());
        candidate.setEmail(candidateRegisterRequest.getEmail());
        candidate.setMobile(candidateRegisterRequest.getMobile());
        candidate.setUser(user);
        candidateRespository.save(candidate);
    }
}
