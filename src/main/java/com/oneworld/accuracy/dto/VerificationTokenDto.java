package com.oneworld.accuracy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerificationTokenDto {
    private Long id;
    private String confirmationToken;
    private Long userId;
    private boolean activated;
    private boolean expired;
    private Date expiryDate;
}
