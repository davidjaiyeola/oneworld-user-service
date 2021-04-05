package com.oneworld.accuracy.dto;

import com.oneworld.accuracy.model.UserRole;
import com.oneworld.accuracy.model.UserStatus;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private Long id;
    private UserStatus status;
    private UserRole role;
    private String title;
    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private String password;
    private boolean Verified;
    private Date dateRegistered;
    private Date dateVerified;
    private Date dateDeactivated;
}
