package com.oneworld.accuracy.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.oneworld.accuracy.model.User;
import com.oneworld.accuracy.model.UserRole;
import com.oneworld.accuracy.model.UserStatus;
import com.oneworld.accuracy.repository.UserRepository;
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
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceUnitTests {

    private static User user1;
    private static User user2;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }


    @BeforeAll
    public static void init() {
        user1 = new User(UserStatus.REGISTERED, UserRole.USER,"Test","Registered","oneaccuracy@gmail.com");
        user2 = new User(UserStatus.VERIFIED, UserRole.USER,"Test","Verified","oneaccuracy2@gmail.com");
    }

    @Test
    public void findById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        assertThat(userService.findById(1L), is(Optional.of(user1)));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void createOrUpdate() {

    }

    @Test
    void deleteById() {
        userService.deleteById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}
