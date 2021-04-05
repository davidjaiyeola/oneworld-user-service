package com.oneworld.accuracy.repository;

import com.oneworld.accuracy.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> getVerificationTokenByConfirmationToken(String confirmationToken);
}
