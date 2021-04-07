package com.oneworld.accuracy.repository;

import com.oneworld.accuracy.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    @Query("SELECT v FROM VerificationToken v WHERE v.confirmationToken = ?1")
    Optional<VerificationToken> getByConfirmationToken(String confirmationToken);

    @Query("SELECT v FROM VerificationToken v WHERE v.userId = ?1 and v.expired=false")
    Optional<VerificationToken> getTokenByUserId(Long userId);
}
