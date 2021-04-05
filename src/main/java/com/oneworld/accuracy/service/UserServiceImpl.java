package com.oneworld.accuracy.service;

import com.oneworld.accuracy.dto.UserCreateDto;
import com.oneworld.accuracy.dto.UserDto;
import com.oneworld.accuracy.dto.UserUpdateDto;
import com.oneworld.accuracy.model.User;
import com.oneworld.accuracy.model.UserStatus;
import com.oneworld.accuracy.model.VerificationToken;
import com.oneworld.accuracy.repository.UserRepository;
import com.oneworld.accuracy.repository.VerificationTokenRepository;
import com.oneworld.accuracy.util.DataValidationException;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private MailContentBuilderService mailContentBuilderService;
    private EmailService emailService;
    @Value("${one-accuracy.confirmToken}")
    protected String confirmToken;
    @Value("${one-accuracy.enableEmail}")
    protected boolean enableEmail;

    public UserServiceImpl(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, MailContentBuilderService mailContentBuilderService, EmailService emailService) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailContentBuilderService = mailContentBuilderService;
        this.emailService = emailService;
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
        user.setStatus(UserStatus.REGISTERED);
        user.setDateRegistered(new Date());
        user = userRepository.save(user);
        VerificationToken token = createToken(user.getId());

        if(enableEmail) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("name", user.getFullName());
            model.put("date", LocalDate.now().toString());
            String url = confirmToken + "/" + token.getConfirmationToken();
            model.put("CallbackUrl", url);
            String content = mailContentBuilderService.build(model, "UserConfirmation");
            content = content.replaceAll("CallbackUrl", confirmToken);
            emailService.sendEmail(null, null, content, "Email Verification", user.getFullName(), null);
        }

        return user;
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
        if(enableEmail) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("name", user.getFullName());
            String content = mailContentBuilderService.build(model, "DeactivationNotification");
            emailService.sendEmail(null, null, content, "Password Reset", user.getFullName(), null);
        }
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
    public User verifyUserByToken(String token) {
        Optional<VerificationToken> verificationTokenOptional  = verificationTokenRepository.getVerificationTokenByConfirmationToken(token);
        if (!verificationTokenOptional.isPresent()) {
            String error = "Token with id " + token + " does not exist.";
            log.error(error);
            throw new DataValidationException(error);
        }
        VerificationToken verificationToken = verificationTokenOptional.get();
        if(verificationToken.isActivated()){
            throw new DataValidationException("Token already used");
        }

        if(verificationToken.isExpired()){
            throw new DataValidationException("Token expired");
        }

        if(verificationToken.getExpiryDate().getTime() < new Date().getTime()){
            verificationToken.setActivated(true);
            verificationToken.setExpired(true);
            verificationTokenRepository.save(verificationToken);
            throw new DataValidationException("Token expired");
        }

        Optional<User> userOptional = findById(verificationToken.getUserId());
        User user = userOptional.get();
        user.setDateVerified(new Date());
        user.setVerified(true);
        user.setStatus(UserStatus.VERIFIED);
        userRepository.save(user);

        verificationToken.setActivated(true);
        verificationToken.setExpired(true);
        verificationTokenRepository.save(verificationToken);

        if(enableEmail) {
            HashMap<String, Object> model = new HashMap<>();
            model.put("name", user.getFullName());
            String content = mailContentBuilderService.build(model, "VerificationNotification");
            emailService.sendEmail(null, null, content, "Password Reset", user.getFullName(), null);
        }

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

    protected VerificationToken createToken(Long userId){
        VerificationToken verificationToken =  new VerificationToken();
        verificationToken.setActivated(false);
        //@TODO encode to base64
        verificationToken.setConfirmationToken(UUID.randomUUID().toString());
        verificationToken.setExpired(false);
        verificationToken.setUserId(userId);
        verificationToken.setExpiryDate(DateUtils.addHours(new Date(), 1));

        verificationTokenRepository.save(verificationToken);

        return verificationToken;

    }

}
