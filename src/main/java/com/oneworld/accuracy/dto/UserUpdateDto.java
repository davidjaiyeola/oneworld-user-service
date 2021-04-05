package com.oneworld.accuracy.dto;

import com.oneworld.accuracy.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDto {
    private UserRole role;
    private String title;
    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private String password;
}
