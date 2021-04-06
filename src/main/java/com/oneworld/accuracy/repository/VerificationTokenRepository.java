package com.oneworld.accuracy.repository;

import com.oneworld.accuracy.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    @Query("SELECT v FROM VerificationToken v WHERE v.confirmationToken = ?1")
    Optional<VerificationToken> getByConfirmationToken(String confirmationToken);

//    @Query("SELECT u FROM VerificationToken u WHERE u.confirmationToken = :confirmationToken")
//    Optional<VerificationToken>  getByConfirmationToken(@Param("confirmationToken") String confirmationToken);
}
