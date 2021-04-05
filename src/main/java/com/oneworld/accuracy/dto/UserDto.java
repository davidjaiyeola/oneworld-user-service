package com.oneworld.accuracy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String status;
    private String role;
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
