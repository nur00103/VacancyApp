package com.example.vacancyapp.confirmation;

import com.example.vacancyapp.confirmation.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationToken,Long> {

    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByEmail(String email);


}
