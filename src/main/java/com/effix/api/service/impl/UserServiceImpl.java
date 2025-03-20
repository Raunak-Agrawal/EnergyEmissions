package com.effix.api.service.impl;

import com.effix.api.dto.request.user.CreateUserRequestDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.exception.EntityExistsException;
import com.effix.api.exception.ForbiddenException;
import com.effix.api.exception.NotFoundException;
import com.effix.api.model.entity.UserModel;
import com.effix.api.repository.UserRepository;
import com.effix.api.service.UserService;
import com.effix.api.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserModel createUser(CreateUserRequestDTO userRequestDTO) throws EntityExistsException {
        UserModel user = userRepository.findByEmail(userRequestDTO.getEmail());

        if (user != null) {
            throw new EntityExistsException("User is already registered with email address");
        }

        user = UserModel.builder()
                .firstName(userRequestDTO.getFirstName())
                .lastName(userRequestDTO.getLastName())
                .email(userRequestDTO.getEmail())
                .password(PasswordUtil.encrypt(userRequestDTO.getPassword()))
                .verified(false)
                .isSuper(true)
                .build();

        userRepository.save(user);
        return user;
    }

    @Override
    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updatePassword(String email, String newPassword) throws NotFoundException {
        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("User with given email not found");
        }
        user.setPassword(PasswordUtil.encrypt(newPassword));
        userRepository.save(user);
    }

    @Override
    public UserModel getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        return userRepository.getById(Long.parseLong(userId));
    }

    @Override
    public boolean verifyUser(String email) throws NotFoundException, BadRequestException {
        if (email == null) {
            throw new NotFoundException("User does not exist");
        }

        UserModel user = userRepository.findByEmail(email);

        if (user == null) {
            throw new NotFoundException("User does not exist");
        }
        if (user.isVerified()) {
            throw new BadRequestException("User already verified");
        }

        user.setVerified(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public UserModel findUserById(Long userId) {
        return userRepository.getById(userId);
    }

    @Override
    public UserModel checkUser(String email, String password) throws NotFoundException, BadRequestException, ForbiddenException {
        log.debug("Retrieving the user: {}", email);
        UserModel user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("User doesn't exist");
        }
        boolean isCredentialValid = PasswordUtil.decrypt(user.getPassword()).equals(password);
        if (!isCredentialValid) throw new BadRequestException("Invalid credentials");

        boolean isUserVerified = user.isVerified();
        if (!isUserVerified) throw new ForbiddenException("User is not verified");

        return user;
    }

    @Override
    public List<UserModel> getAllUsers(){
        return userRepository.findAll();
    }
}
