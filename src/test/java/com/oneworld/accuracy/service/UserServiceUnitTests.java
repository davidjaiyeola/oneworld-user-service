package com.oneworld.accuracy.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceUnitTests {

    private static User user1;
    private static User user2;

    @Mock
    private Pageable pageableMock;
    @Mock
    private Page<User> userPage;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll_Paginated_When_No_Record() {
        Pageable paging = PageRequest.of(0, 10, Sort.by("id"));
        Mockito.when(userRepository.findAll(paging)).thenReturn(userPage);
        assertThat(userService.findAllPaginated(0,10,"id" ).size(), is(0));
        Mockito.verify(userRepository, Mockito.times(1)).findAll(paging);
    }
    @Test
    public void findAll_When_No_Record() {
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList());
        assertThat(userService.findAll().size(), is(0));
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void findAll_When_There_Is_Record() {
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user1,user2));
        assertThat(userService.findAll().size(), is(2));
        assertThat(userService.findAll().get(0), is(user1));
        assertThat(userService.findAll().get(1), is(user2));
        Mockito.verify(userRepository, Mockito.times(3)).findAll();
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
        Mockito.when(userRepository.save(user1)).thenReturn(user1);
        assertThat(userService.createOrUpdate(user1), is(user1));
        Mockito.verify(userRepository, Mockito.times(1)).save(user1);

        Mockito.when(userRepository.save(user2)).thenReturn(user2);
        assertThat(userService.createOrUpdate(user2).getFirstname(), is("Test"));
        Mockito.verify(userRepository, Mockito.times(1)).save(user2);

    }

    @Test
    void deleteById() {
        userService.deleteById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}
