package com.effix.api.controller.v1;

import com.effix.api.dto.request.user.CreateUserRequestDTO;
import com.effix.api.dto.request.user.ForgotPasswordRequestDTO;
import com.effix.api.dto.request.user.LoginUserRequestDTO;
import com.effix.api.dto.request.user.ResetPasswordRequestDTO;
import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.dto.response.user.CreateUserResponseDTO;
import com.effix.api.dto.response.user.LoginUserResponseDTO;
import com.effix.api.exception.*;
import com.effix.api.mappers.UserMapper;
import com.effix.api.model.entity.CompanyProfile;
import com.effix.api.model.entity.Payment;
import com.effix.api.model.entity.UserModel;
import com.effix.api.service.CompanyProfileService;
import com.effix.api.service.PaymentService;
import com.effix.api.service.UserService;
import com.effix.api.service.impl.MailContentBuilder;
import com.effix.api.service.impl.MailService;
import com.effix.api.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyProfileService companyProfileService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private MailService mailService;

    @Value("${frontend-domain}")
    private String frontendDomain;

    @Value("${jwt.secret.access.token}")
    private String jwtSecretAccessToken;

    @Value("${jwt.secret.verify.token}")
    private String jwtSecretVerifyToken;

    @Value("${jwt.secret.forgotpassword.token}")
    private String jwtSecretForgotPasswordToken;

    @Value("${cookie.expiration.seconds}")
    private int cookieExpirationSeconds;

    @PostMapping
    ResponseEntity<ResponseDTO<CreateUserResponseDTO>> createUser(@RequestBody @Valid CreateUserRequestDTO userRequestDTO) throws EntityExistsException {
        UserModel user = userService.createUser(userRequestDTO);
        if (user != null) {
            String verificationToken = jwtTokenUtil.generateToken(userRequestDTO.getEmail(), jwtSecretVerifyToken);
            Map<String, Object> variables = new HashMap<>();
            variables.put("verificationUrl", frontendDomain + "/auth/verify?token=" + verificationToken);
            String message = mailContentBuilder.generateMailContent(variables, "verifyEmailTemplate");
            mailService.sendMail("support@accounting.bi", userRequestDTO.getEmail(), "Account Activation", message, message);

            CreateUserResponseDTO userResponseDTO = userMapper.userEntityToCreateUserResponseDTO(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDTO.success("User created successfully", userResponseDTO));
        }
        return ResponseEntity.internalServerError().body(ResponseDTO.failure("Not able to create user"));
    }

    @GetMapping("/verify")
    public ResponseEntity<ResponseDTO<?>> verify(@RequestParam String token) throws NotFoundException, BadRequestException {
        String email = jwtTokenUtil.getSubjectIfValidToken(token, jwtSecretVerifyToken);
        if (email == null) {
            throw new BadRequestException("Verify token not valid");
        }
        boolean verified = userService.verifyUser(email);
        if (verified) {
            return ResponseEntity.ok(ResponseDTO.success("User verified successfully"));
        } else return ResponseEntity.internalServerError().body(ResponseDTO.failure("Not able to verify user"));
    }

    @PostMapping("/login")
    ResponseEntity<ResponseDTO<LoginUserResponseDTO>> loginUser(@RequestBody @Valid LoginUserRequestDTO userRequestDTO) throws NotFoundException, BadRequestException, ForbiddenException {
        UserModel user = userService.checkUser(userRequestDTO.getEmail(), userRequestDTO.getPassword());
        if (user != null) {
            String authToken = jwtTokenUtil.generateToken(user.getId().toString(), jwtSecretAccessToken);
            String redirectPath = getPostLoginRedirectPath(user);

            LoginUserResponseDTO responseDTO = LoginUserResponseDTO.builder()
                    .email(user.getEmail())
                    .redirectPath(redirectPath)
                    .accessToken("Bearer " + authToken)
                    .build();

//            Cookie cookie = new Cookie("auth_token", authToken);
//            cookie.setHttpOnly(true);
//            cookie.setPath("/");
//            cookie.setSecure(true);
//            cookie.setMaxAge(cookieExpirationSeconds); // Same as jwt expiration i.e. 1 day
//            response.addCookie(cookie);
            return ResponseEntity.ok(ResponseDTO.success("User login success", responseDTO));
        }
        return ResponseEntity.internalServerError().body(ResponseDTO.failure("User login failed"));
    }

    @PostMapping("/forgotpassword")
    ResponseEntity<ResponseDTO<?>> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO forgotPasswordRequestDTO) throws NotFoundException {
        UserModel user = userService.findByEmail(forgotPasswordRequestDTO.getEmail());
        if (user == null) {
            throw new NotFoundException("User with given email not found");
        }
        String forgotPasswordToken = jwtTokenUtil.generateToken(forgotPasswordRequestDTO.getEmail(), jwtSecretForgotPasswordToken);
        Map<String, Object> variables = new HashMap<>();
        variables.put("verificationUrl", frontendDomain + "/auth/resetpassword?token=" + forgotPasswordToken);
        String message = mailContentBuilder.generateMailContent(variables, "forgotPasswordEmailTemplate");
        mailService.sendMail("support@accounting.bi", forgotPasswordRequestDTO.getEmail(), "Reset Password", message, message);

        return ResponseEntity.ok(ResponseDTO.success("Password reset link sent successfully"));
    }

    @PostMapping("/resetpassword")
    ResponseEntity<ResponseDTO<?>> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequestDTO) throws NotFoundException, BadRequestException {
        String email = jwtTokenUtil.getSubjectIfValidToken(resetPasswordRequestDTO.getToken(), jwtSecretForgotPasswordToken);
        if (email == null) {
            throw new BadRequestException("Reset token not valid");
        }
        userService.updatePassword(email, resetPasswordRequestDTO.getPassword());
        return ResponseEntity.ok(ResponseDTO.success("Password reset success. Please login to continue."));
    }

    @PostMapping("/logout")
    ResponseEntity<ResponseDTO<?>> logout(HttpServletResponse response) {
//        Cookie cookie = new Cookie("auth_token", null);
//        cookie.setMaxAge(0);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setSecure(true);
//        response.addCookie(cookie);
        return ResponseEntity.ok(ResponseDTO.success("Logout user success. Please login to continue."));
    }

    @GetMapping("/isloggedin")
    ResponseEntity<ResponseDTO<?>> isLoggedIn(HttpServletResponse response) throws UnAuthorizedException {
        UserModel userModel = userService.getLoggedInUser();
        if(userModel != null) {
            throw new UnAuthorizedException("User is not logged in");
        }
        return ResponseEntity.ok(ResponseDTO.success("User is logged in"));
    }

    private String getPostLoginRedirectPath(UserModel user) {
        String path = "";

        CompanyProfile companyProfile = companyProfileService.getCompanyProfile(user);
        Payment paymentInfo = paymentService.getPayment(companyProfile);

        if (companyProfile == null) {
            path = "/user/profile/companyprofile";
        }
        else if(paymentInfo == null){
            path = "/user/profile/chooseplan";
        }
        else {
            path = "/dashboard";
        }

        return path;
    }
}
