package com.oneworld.accuracy.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
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
