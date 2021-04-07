package com.oneworld.accuracy.controllers;

import com.oneworld.accuracy.dto.UserCreateDto;
import com.oneworld.accuracy.dto.UserDto;
import com.oneworld.accuracy.dto.UserUpdateDto;
import com.oneworld.accuracy.model.User;
import com.oneworld.accuracy.model.UserRole;
import com.oneworld.accuracy.model.UserStatus;
import com.oneworld.accuracy.service.UserServiceImpl;
import com.oneworld.accuracy.util.DataValidationException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserControllerUnitTest {

    private static User user1;
    private static UserDto dto;
    private static UserCreateDto userCreateDto;
    private static UserUpdateDto userUpdateDto;
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void init() {
        user1 = new User(UserStatus.REGISTERED, UserRole.USER,"Test","Registered","oneaccuracy@gmail.com");
        userCreateDto = new UserCreateDto(UserRole.USER, "Mister","David", "Jaiyeola", "oneaccuracy@gmail.com", "090", "9899");
        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("oneaccuracy2228@gmail.com");
        userUpdateDto.setFirstname("Jaiyeola");
        userUpdateDto.setLastname("Opeyemi");
        userUpdateDto.setPassword("0ij");
        userUpdateDto.setMobile("090");
        userUpdateDto.setTitle("Mister");
        userUpdateDto.setRole(UserRole.USER);
        dto = new UserDto();
        dto.setTitle(user1.getTitle());
        dto.setDateDeactivated(user1.getDateDeactivated());
        dto.setDateRegistered(user1.getDateRegistered());
        dto.setStatus((user1.getStatus()== null?null:user1.getStatus().getName()));
        dto.setRole((user1.getRole()== null?null:user1.getRole().getName()));
        dto.setEmail(user1.getEmail());
        dto.setDateVerified(user1.getDateVerified());
        dto.setFirstname(user1.getFirstname());
        dto.setLastname(user1.getLastname());
        dto.setPassword(user1.getPassword());
        dto.setMobile(user1.getMobile());
        dto.setVerified(user1.isVerified());
        dto.setId(user1.getId());
    }

    @Test
    void create() {
        Mockito.when(userService.findByEmail("oneaccuracy@gmail.com")).thenReturn(Optional.of(user1));
        ResponseEntity<UserDto> responseEntity = userController.create(userCreateDto);
        assertThat(responseEntity.getStatusCode(),is(HttpStatus.OK));
        Mockito.verify(userService, Mockito.times(1)).findByEmail("oneaccuracy@gmail.com");
    }

    @Test
    void updateWhenUserNotFound() {
        Mockito.when(userService.findById(1L)).thenReturn(Optional.empty());
        AtomicReference<ResponseEntity<UserDto>> userDtoResponseEntity = null;
        Exception exception = assertThrows(DataValidationException.class, () -> {
            userDtoResponseEntity.set(userController.update(1L, userUpdateDto));
        });
        assertTrue(exception instanceof DataValidationException);
    }

    @Test
    void updateWhenUserFound() {

        Mockito.when(userService.findById(1L)).thenReturn(Optional.of(user1));

        Mockito.when(userService.updateDtoToEntity(userUpdateDto, user1)).thenReturn(user1);
        ResponseEntity<UserDto> responseEntity = userController.update(1L, userUpdateDto);
        assertThat(responseEntity.getStatusCode(),is(HttpStatus.OK));
        Mockito.verify(userService, Mockito.times(1)).createOrUpdate(user1);
    }
}
