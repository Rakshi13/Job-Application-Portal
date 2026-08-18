package com.rakshith.JobApplication.Repository;

import com.rakshith.JobApplication.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    //what if user doesn't exist , we are using optional
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
