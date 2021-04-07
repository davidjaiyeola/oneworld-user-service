package com.oneworld.accuracy.service;

import com.oneworld.accuracy.model.User;
import com.oneworld.accuracy.model.UserRole;
import com.oneworld.accuracy.model.UserStatus;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MailContentBuilderServiceUnitTests {

    @Autowired
    private MailContentBuilderService mailContentBuilderService;

    private static User user1;
    private static User user2;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @BeforeAll
    public static void init() {
        user1 = new User(UserStatus.REGISTERED, UserRole.USER,"David","J","oneaccuracy@gmail.com");
        user2 = new User(UserStatus.VERIFIED, UserRole.USER,"Test","Verified","oneaccuracy2@gmail.com");
    }

    @Test
    public void returnsEmptyStringWhenModelIsNull() {
        String content = mailContentBuilderService.build(null, "UserConfirmation");
        assertThat(content, is(""));
    }

    @Test
    public void contentContainsString() {
        HashMap<String, Object> model = new HashMap<>();
        model.put("name", user1.getFullName());
        model.put("date", LocalDate.now().toString());
        String url = "http" + "/" + "token";
        model.put("CallbackUrl", url);
        String content = mailContentBuilderService.build(model, "UserConfirmation");
        content = content.replaceAll("CallbackUrl", "yyyy");
        assertThat(content, containsString("David J"));
    }
}
