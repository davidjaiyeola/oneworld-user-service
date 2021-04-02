package com.oneworld.accuracy.dto;

import com.oneworld.accuracy.model.UserRole;
import com.oneworld.accuracy.model.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private UserStatus status;
    private UserRole role;
    private String title;
    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private String password;
    private boolean Verified;
    private LocalDateTime dateRegistered;
    private LocalDateTime dateVerified;
    private LocalDateTime dateDeactivated;
}
