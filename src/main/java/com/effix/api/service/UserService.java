package com.effix.api.service;

import com.effix.api.dto.request.user.CreateUserRequestDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.exception.EntityExistsException;
import com.effix.api.exception.ForbiddenException;
import com.effix.api.exception.NotFoundException;
import com.effix.api.model.entity.UserModel;

import java.util.List;

public interface UserService {
    UserModel createUser(CreateUserRequestDTO userRequestDTO) throws EntityExistsException;

    UserModel checkUser(String email, String password) throws NotFoundException, BadRequestException, ForbiddenException;

    List<UserModel> getAllUsers();

    boolean verifyUser(String email) throws NotFoundException, BadRequestException;

    UserModel findUserById(Long userId);

    UserModel findByEmail(String email);

    void updatePassword(String email, String newPassword) throws NotFoundException;

    UserModel getLoggedInUser();
}
