package com.oneworld.accuracy.controllers;

import com.oneworld.accuracy.dto.ExceptionDto;
import com.oneworld.accuracy.dto.UserCreateDto;
import com.oneworld.accuracy.dto.UserDto;
import com.oneworld.accuracy.dto.UserUpdateDto;
import com.oneworld.accuracy.model.User;
import com.oneworld.accuracy.service.UserService;
import com.oneworld.accuracy.util.DataValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/api"})
@Slf4j
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(Throwable.class)
    public ExceptionDto handleException(Throwable ex) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setValid(false);
        exceptionDto.setErrorDescription(ex.getMessage());
        if (ex instanceof DataValidationException) {
            exceptionDto.setError("Data Validation");
            exceptionDto.setResponseCode("22");
        } else {
            exceptionDto.setError("Internal Server Error");
            exceptionDto.setResponseCode("99");
        }

        return exceptionDto;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy)
    {
        List<UserDto> users = userService.findAllPaginated(pageNo, pageSize, sortBy).stream().map(user->
                userService.entityToDto(user)).collect(Collectors.toList());
        return new ResponseEntity<>(users, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(userService.entityToDto(userService.createOrUpdate(userService.createDtoToEntity(userCreateDto))));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable long id) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            String error = "User with id " + id + " does not exist.";
            log.error(error);
            throw new DataValidationException(error);
        }
        return ResponseEntity.ok(userService.entityToDto(user.get()));
    }

    @GetMapping("/user/verify/{token}")
    public ResponseEntity<UserDto> verifyUserById(@PathVariable String token) {
        return ResponseEntity.ok(userService.entityToDto(userService.verifyUserByToken(token)));
    }

    @PutMapping(value="/user/{id}")
    public ResponseEntity<UserDto> update(@PathVariable long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            String error = "User with id " + id + " does not exist.";
            log.error(error);
            throw new DataValidationException(error);
        }

        return ResponseEntity.ok(userService.entityToDto(userService.createOrUpdate(userService.updateDtoToEntity(userUpdateDto, user.get()))));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deactivateById(@PathVariable long id) throws DataValidationException {
        Optional<User> p = userService.findById(id);
        if (!p.isPresent()) {
            String error = "User with id " + id + " does not exist.";
            log.error(error);
            throw new DataValidationException(error);
        }

        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/user/deleteFromDB/{id}")
    public ResponseEntity deleteById(@PathVariable long id) {
        Optional<User> p = userService.findById(id);

        if (!p.isPresent()) {
            String error = "User with id " + id + " does not exist.";
            log.error(error);
            throw new DataValidationException(error);
        }

        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
