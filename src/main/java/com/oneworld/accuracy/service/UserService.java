package com.oneworld.accuracy.service;

import com.oneworld.accuracy.dto.UserCreateDto;
import com.oneworld.accuracy.dto.UserDto;
import com.oneworld.accuracy.dto.UserUpdateDto;
import com.oneworld.accuracy.dto.VerificationTokenDto;
import com.oneworld.accuracy.model.User;
import com.oneworld.accuracy.model.VerificationToken;
import com.oneworld.accuracy.util.DataValidationException;

import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> findAllPaginated(Integer pageNo, Integer pageSize, String sortBy);
    List<User> findAll();
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    void deleteById(long id);
    User createOrUpdate(User user);
    void deactivateUser(long id) throws DataValidationException;
    UserDto entityToDto(User user);
    User createDtoToEntity(UserCreateDto dto, User user);
    User updateDtoToEntity(UserUpdateDto dto, User user);

    VerificationToken getTokenByUserId(Long userId);
    VerificationTokenDto verificationTokenToDto(VerificationToken verificationToken);
    User verifyUserByToken(String token);
}
