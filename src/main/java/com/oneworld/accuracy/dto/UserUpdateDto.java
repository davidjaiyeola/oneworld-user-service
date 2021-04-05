package com.oneworld.accuracy.dto;

import com.oneworld.accuracy.model.UserRole;
import lombok.Data;

//@AllArgsConstructor
@Data
public class UserUpdateDto {
    private UserRole role;
    private String title;
    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private String password;

    public UserUpdateDto() {
    }
}
