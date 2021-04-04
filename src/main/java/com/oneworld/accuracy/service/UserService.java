package com.oneworld.accuracy.service;

import com.oneworld.accuracy.model.User;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;


public interface UserService {
    List<User> findAllPaginated(Integer pageNo, Integer pageSize, String sortBy);
    List<User> findAll();
    Optional<User> findById(long id);
    void deleteById(long id);
    User createOrUpdate(User product);
    void deactivateUser(long id) throws ValidationException;
}
