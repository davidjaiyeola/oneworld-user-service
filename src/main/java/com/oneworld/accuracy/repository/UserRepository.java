package com.oneworld.accuracy.repository;

import com.oneworld.accuracy.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
 Optional<User> findByEmail(String email);
}
