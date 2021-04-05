package com.oneworld.accuracy.dto;

import com.oneworld.accuracy.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    private UserRole role;
    private String title;
    private String firstname;
    private String lastname;
    @NotEmpty
    private String email;
    private String mobile;
    private String password;
}
