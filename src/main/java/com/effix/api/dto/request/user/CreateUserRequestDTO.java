package com.effix.api.dto.request.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDTO {

    @NotBlank(message = "first name can not be blank")
    private String firstName;

    @NotBlank(message = "last name can not be blank")
    private String lastName;

    @NotBlank(message = "password can not be blank")
    private String password;

    @NotBlank(message = "email can not be blank")
    @Email(message = "invalid email format")
    private String email;
}
