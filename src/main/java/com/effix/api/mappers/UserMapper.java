package com.effix.api.mappers;

import com.effix.api.dto.response.user.CreateUserResponseDTO;
import com.effix.api.model.entity.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    CreateUserResponseDTO userEntityToCreateUserResponseDTO(UserModel user);
}