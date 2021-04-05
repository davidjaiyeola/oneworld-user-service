package com.oneworld.accuracy.service;

import com.oneworld.accuracy.dto.UserCreateDto;
import com.oneworld.accuracy.dto.UserDto;
import com.oneworld.accuracy.dto.UserUpdateDto;
import com.oneworld.accuracy.model.User;
import com.oneworld.accuracy.model.UserStatus;
import com.oneworld.accuracy.repository.UserRepository;
import com.oneworld.accuracy.util.DataValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAllPaginated(Integer pageNo, Integer pageSize, String sortBy)
    {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<User> pagedResult = userRepository.findAll(paging);

        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User createOrUpdate(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deactivateUser(long id) throws DataValidationException{
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty())
            throw new DataValidationException("No user found");

        User user = optionalUser.get();
        user.setDateDeactivated(new Date());
        user.setStatus(UserStatus.DEACTIVATED);

        userRepository.save(user);
    }

    @Override
    public UserDto entityToDto(User user){
        UserDto dto = new UserDto();
        dto.setTitle(user.getTitle());
        dto.setDateDeactivated(user.getDateDeactivated());
        dto.setDateRegistered(user.getDateRegistered());
        dto.setStatus((user.getStatus()== null?null:user.getStatus().getName()));
        dto.setRole((user.getRole()== null?null:user.getRole().getName()));
        dto.setEmail(user.getEmail());
        dto.setDateVerified(user.getDateVerified());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setPassword(user.getPassword());
        dto.setMobile(user.getMobile());
        dto.setVerified(user.isVerified());
        dto.setId(user.getId());
        return dto;
    }

    @Override
    public User updateDtoToEntity(UserUpdateDto dto, User user){
        user.setTitle(dto.getTitle());
        user.setEmail(dto.getEmail());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setPassword(dto.getPassword());
        user.setMobile(dto.getMobile());
        return user;
    }

    @Override
    public User createDtoToEntity(UserCreateDto dto){
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setPassword(dto.getPassword());
        user.setMobile(dto.getMobile());
        user.setTitle(dto.getTitle());
        return user;
    }

}
