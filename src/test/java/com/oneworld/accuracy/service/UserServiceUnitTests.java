package com.oneworld.accuracy.service;

import com.oneworld.accuracy.dto.UserCreateDto;
import com.oneworld.accuracy.dto.UserDto;
import com.oneworld.accuracy.dto.UserUpdateDto;
import com.oneworld.accuracy.model.User;
import com.oneworld.accuracy.model.UserRole;
import com.oneworld.accuracy.model.UserStatus;
import com.oneworld.accuracy.model.VerificationToken;
import com.oneworld.accuracy.repository.UserRepository;
import com.oneworld.accuracy.repository.VerificationTokenRepository;
import com.oneworld.accuracy.util.DataValidationException;
import org.apache.commons.lang.time.DateUtils;
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
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceUnitTests {

    private static User user1;
    private static User user2;
    private static User user3;
    private static User user4;
    private static VerificationToken verificationToken1;
    private static VerificationToken verificationToken2;
    private static UserDto userDto1;
    private static UserCreateDto userCreateDto1;
    private static UserUpdateDto userUpdateDto1;
    @Mock
    private Page<User> userPage;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VerificationTokenRepository verificationTokenRepository;
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
        user3 = new User(UserStatus.REGISTERED, UserRole.USER,"Test","Registered","oneaccuracy@gmail.com");
        user4 = new User(UserStatus.REGISTERED, UserRole.USER,"Test","Registered","oneaccuracy@gmail.com");
        userDto1 =  new UserDto();
        userDto1.setFirstname("Test");
        userCreateDto1 = new UserCreateDto(UserRole.USER, "Mister","David", "Jaiyeola", "oneaccuracy222@gmail.com", "090", "9899");
        userUpdateDto1 = new UserUpdateDto(UserRole.USER, "Mister","Opeyemi", "Jaiyeola", "oneaccuracy2228@gmail.com", "090", "9899");

        verificationToken1 = new VerificationToken(1l, UUID.randomUUID().toString(),1L,false, false, DateUtils.addHours(new Date(), 1));
    }

    @Test
    public void find_user_by_email() {
        Mockito.when(userRepository.findByEmail("oneaccuracy@gmail.com")).thenReturn(Optional.of(user1));
        assertThat(userService.findByEmail("oneaccuracy@gmail.com").get().getEmail(), is("oneaccuracy@gmail.com"));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail("oneaccuracy@gmail.com");
    }

    @Test
    public void find_all_paginated_when_no_record() {
        Pageable paging = PageRequest.of(0, 10, Sort.by("id"));
        Mockito.when(userRepository.findAll(paging)).thenReturn(userPage);
        assertThat(userService.findAllPaginated(0,10,"id" ).size(), is(0));
        Mockito.verify(userRepository, Mockito.times(1)).findAll(paging);
    }

    @Test
    public void find_all_paginated_when_there_is_record() {
        Pageable paging = PageRequest.of(0, 10, Sort.by("id"));
        Page<User> userPageList = new PageImpl(Arrays.asList(user1,user2));
        Mockito.when(userRepository.findAll(paging)).thenReturn(userPageList);
        assertThat(userService.findAllPaginated(0,10,"id" ).size(), is(2));
        Mockito.verify(userRepository, Mockito.times(1)).findAll(paging);
    }

    @Test
    public void find_all_when_No_Record() {
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList());
        assertThat(userService.findAll().size(), is(0));
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void find_all_when_there_is_record() {
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user1,user2));
        assertThat(userService.findAll().size(), is(2));
        assertThat(userService.findAll().get(0), is(user1));
        assertThat(userService.findAll().get(1), is(user2));
        Mockito.verify(userRepository, Mockito.times(3)).findAll();
    }

    @Test
    public void find_user_by_id() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        assertThat(userService.findById(1L), is(Optional.of(user1)));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void deactivate_user_by_id() throws DataValidationException {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user3));
        userService.deactivateUser(1L);
        assertThat(user3.getStatus(), is(UserStatus.DEACTIVATED));
        assertThat(user3.getDateDeactivated(), is(notNullValue()));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }
    @Test
    void create_or_update_user() {
        Mockito.when(userRepository.save(user1)).thenReturn(user1);
        assertThat(userService.createOrUpdate(user1), is(user1));
        Mockito.verify(userRepository, Mockito.times(1)).save(user1);

        Mockito.when(userRepository.save(user2)).thenReturn(user2);
        assertThat(userService.createOrUpdate(user2).getFirstname(), is("Test"));
        Mockito.verify(userRepository, Mockito.times(1)).save(user2);

    }

    @Test
    void delete_user_by_id() {
        userService.deleteById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void user_to_dto() {
        assertThat(userService.entityToDto(user4).getFirstname(), is(user4.getFirstname()));
    }

    @Test
    public void create_dto_to_entity() {
        assertThat(userService.createDtoToEntity(userCreateDto1,user4).getFirstname(), is(user4.getFirstname()));
    }

    @Test
    public void update_dto_to_entity() {
        assertThat(userService.updateDtoToEntity(userUpdateDto1,user4).getFirstname(), is(user4.getFirstname()));
    }

    @Test
    public void successfully_verify_user_by_token(){
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        Mockito.when(verificationTokenRepository.getByConfirmationToken(verificationToken1.getConfirmationToken())).thenReturn(Optional.of(verificationToken1));
        assertThat(userService.verifyUserByToken(verificationToken1.getConfirmationToken()).isVerified(), is(true));
        Mockito.verify(verificationTokenRepository, Mockito.times(1)).getByConfirmationToken(verificationToken1.getConfirmationToken());
    }
}
