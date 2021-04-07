package com.oneworld.accuracy.controllers;


import com.oneworld.accuracy.dto.UserDto;
import com.oneworld.accuracy.dto.UserCreateDto;
import com.oneworld.accuracy.dto.UserUpdateDto;
import com.oneworld.accuracy.dto.VerificationTokenDto;
import com.oneworld.accuracy.dto.ExceptionDto;
import com.oneworld.accuracy.model.UserRole;
import com.oneworld.accuracy.model.UserStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    private static UserCreateDto userCreateDto1;
    private static UserCreateDto userCreateDto2;
    private static UserCreateDto userCreateDto3;
    private static UserUpdateDto userUpdateDto1;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        userCreateDto1 = new UserCreateDto(UserRole.USER, "Mister","David", "Jaiyeola", "oneaccuracy222@gmail.com", "090", "9899");
        userCreateDto2 = new UserCreateDto(UserRole.USER, "Mister","Shade", "Jaiyeola", "oneaccuracy222@gmail.com", "090", "9899");
        userUpdateDto1 = new UserUpdateDto(UserRole.USER, "Mister","Opeyemi", "Jaiyeola", "oneaccuracy222@gmail.com", "090", "9899");
        userCreateDto3 = new UserCreateDto(UserRole.USER, "Mister","David", "Jaiyeola", "oneaccuracy222@gmail.com", "090", "9899");

    }

    @Test
    public void deactivateUser() {
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto3);
        ResponseEntity<UserDto> response = restTemplate.postForEntity("http://127.0.0.1:"+port+"/api/user", request, UserDto.class);
        long id = response.getBody().getId();
        response = restTemplate.getForEntity("http://127.0.0.1:"+port+"/api/user/"+id, UserDto.class);

        assertThat(response.getBody().getId(), is(id));
        assertThat(response.getBody().getStatus(), is(UserStatus.REGISTERED.getName()));

        //then deactivate
        response = restTemplate.exchange("http://127.0.0.1:"+port+"/api/user/"+id, HttpMethod.DELETE,null, UserDto.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        //Find
        response = restTemplate.getForEntity("http://127.0.0.1:"+port+"/api/user/"+id, UserDto.class);
        assertThat(response.getBody().getId(), is(id));
        assertThat(response.getBody().getStatus(), is(UserStatus.DEACTIVATED.getName()));
        assertThat(response.getBody().getDateDeactivated(), is(notNullValue()));

        //Delete from db
        restTemplate.delete("http://127.0.0.1:"+port+"/user/deleteFromDB/"+id);
    }

    @Test
    public void activateUser() {
        //Create
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto3);
        ResponseEntity<UserDto> response = restTemplate.postForEntity("http://127.0.0.1:"+port+"/api/user", request, UserDto.class);
        long id = response.getBody().getId();
        response = restTemplate.getForEntity("http://127.0.0.1:"+port+"/api/user/"+id, UserDto.class);
        assertThat(response.getBody().getId(), is(id));
        assertThat(response.getBody().getStatus(), is(UserStatus.REGISTERED.getName()));

        //Get Token
        ResponseEntity<VerificationTokenDto> verificationTokenDtoResponseEntity = restTemplate.getForEntity("http://127.0.0.1:"+port+"/api/user/token/"+id, VerificationTokenDto.class);
        assertThat(verificationTokenDtoResponseEntity.getBody().getUserId(), is(id));
        assertTrue(verificationTokenDtoResponseEntity.getBody().getExpiryDate().getTime() > new Date().getTime());
        assertThat(verificationTokenDtoResponseEntity.getBody().getConfirmationToken(), is(notNullValue()));

        //Activate
        response = restTemplate.getForEntity("http://127.0.0.1:"+port+"/api/user/verify/"+verificationTokenDtoResponseEntity.getBody().getConfirmationToken(), UserDto.class);
        assertThat(response.getBody().getId(), is(id));
        assertThat(response.getBody().getStatus(), is(UserStatus.VERIFIED.getName()));
        assertThat(response.getBody().getDateVerified(), is(notNullValue()));

        //Delete from db
        restTemplate.delete("http://127.0.0.1:"+port+"/user/deleteFromDB/"+id);
    }

    @Test
    public void findAllPaginated() {
        ResponseEntity<UserDto[]> result = this.restTemplate
                .getForEntity("http://127.0.0.1:"+port+"/api/users", UserDto[].class);

        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), is(notNullValue()));
    }

    @Test
    public void createNewUser() {
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto1);
        ResponseEntity<UserDto> response = restTemplate.postForEntity("http://127.0.0.1:"+port+"/api/user", request, UserDto.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getFirstname(), is("David"));

        restTemplate.delete("http://127.0.0.1:"+port+"/user/deleteFromDB/"+response.getBody().getId());
    }

    @Test
    public void updateUser() {
        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto2);
        ResponseEntity<UserDto> response = restTemplate.postForEntity("http://127.0.0.1:"+port+"/api/user", request, UserDto.class);

        long id = response.getBody().getId();

        HttpEntity<UserUpdateDto> updateRequest = new HttpEntity<>(userUpdateDto1);
        response = restTemplate.exchange("http://127.0.0.1:"+port+"/api/user/"+id, HttpMethod.PUT, updateRequest, UserDto.class);

        assertThat(response.getBody().getFirstname(), is("Opeyemi"));
        assertThat(response.getBody().getId(), is(id));
        restTemplate.delete("http://127.0.0.1:"+port+"/user/deleteFromDB/"+id);
    }

    @Test
    public void findByID() {

        HttpEntity<UserCreateDto> request = new HttpEntity<>(userCreateDto3);
        ResponseEntity<UserDto> response = restTemplate.postForEntity("http://127.0.0.1:"+port+"/api/user", request, UserDto.class);
        long id = response.getBody().getId();
        response = restTemplate.getForEntity("http://127.0.0.1:"+port+"/api/user/"+id, UserDto.class);

        assertThat(response.getBody().getId(), is(id));
        assertThat(response.getBody().getFirstname(), equalTo(userCreateDto3.getFirstname()));
        restTemplate.delete("http://127.0.0.1:"+port+"/user/deleteFromDB/"+id);
    }

    @Test
    public void dataValidationGlobalError() {
        ResponseEntity<ExceptionDto> response = restTemplate.getForEntity("http://127.0.0.1:"+port+"/api/user/"+4552, ExceptionDto.class);
        assertThat(response.getBody().getResponseCode(), is("22"));
        assertThat(response.getBody().getError(), is("Data Validation"));
    }

}
