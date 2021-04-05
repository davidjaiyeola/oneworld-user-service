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
    private static User user2;
    private static User user3;
    private static UserDto userDto;
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
        user2 = new User(UserStatus.VERIFIED, UserRole.USER,"Test","Verified","oneaccuracy2@gmail.com");
        user3 = new User(UserStatus.REGISTERED, UserRole.ADMIN,"Opeyemi","Admin","oneaccuracy22@gmail.com");
        userCreateDto = new UserCreateDto(UserRole.USER, "Mister","David", "Jaiyeola", "oneaccuracy222@gmail.com", "090", "9899");
//        userUpdateDto = new UserUpdateDto(UserRole.USER, "Mister","Opeyemi", "Jaiyeola", "oneaccuracy2228@gmail.com", "090", "9899");
        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("oneaccuracy2228@gmail.com");
        userUpdateDto.setFirstname("Jaiyeola");
        userUpdateDto.setLastname("Opeyemi");
        userUpdateDto.setPassword("0ij");
        userUpdateDto.setMobile("090");
        userUpdateDto.setTitle("Mister");
        userUpdateDto.setRole(UserRole.USER);
//        userDto = new UserDto(UserStatus.REGISTERED, UserRole.ADMIN,"Opeyemi","Admin","oneaccuracy22@gmail.com");

    }

    @Test
    void create() {
        ResponseEntity<UserDto> create1 = userController.create(userCreateDto);
        Mockito.verify(userService, Mockito.times(1)).createOrUpdate(userService.createDtoToEntity(userCreateDto, null));
    }

    @Test
    void update_when_user_not_found() {
        Mockito.when(userService.findById(1L)).thenReturn(Optional.empty());
        AtomicReference<ResponseEntity<UserDto>> userDtoResponseEntity = null;
        Exception exception = assertThrows(DataValidationException.class, () -> {
            userDtoResponseEntity.set(userController.update(1L, userUpdateDto));
        });
        assertTrue(exception instanceof DataValidationException);
    }

    @Test
    void update_when_user_found() {

        Mockito.when(userService.findById(1L)).thenReturn(Optional.of(user1));

        Mockito.when(userService.createOrUpdate(user1)).thenReturn(user3);
        assertThat(userController.update(1L, userUpdateDto).getBody().getFirstname(), is("Opeyemi"));
        Mockito.verify(userService, Mockito.times(1)).createOrUpdate(user1);
    }
}
