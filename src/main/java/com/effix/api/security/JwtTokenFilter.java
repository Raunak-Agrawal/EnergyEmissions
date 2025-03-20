package com.effix.api.security;

import com.effix.api.dto.response.ResponseDTO;
import com.effix.api.exception.BadRequestException;
import com.effix.api.model.entity.UserModel;
import com.effix.api.service.UserService;
import com.effix.api.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Configuration
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.secret.access.token}")
    private String jwtSecretAccessToken;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {

        try {
//            Optional<String> tokenFromCookie = readCookie(request, "auth_token");
//
//            if (tokenFromCookie.isEmpty()) {
//                throw new BadRequestException("Auth token not present");
//            }
//
//            String token = tokenFromCookie.get();

            final String header = request.getHeader("Authorization");

            if (header == null || !header.startsWith("Bearer ")) {
                throw new BadRequestException("Authorization header not present");
            }

            final String token = header.split(" ")[1].trim();

            String userId = jwtTokenUtil.getSubjectIfValidToken(token, jwtSecretAccessToken);
            if (userId == null) {
                throw new BadRequestException("Authorization header not valid");
            }

            UserModel userModel = userService.findUserById(Long.parseLong(userId));

            if (userModel == null) {
                throw new BadRequestException("Authorization header not valid");
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, null);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (BadRequestException e) {
            logger.error(e.getMessage());

            ResponseDTO<Object> errorResponse = ResponseDTO.failure(e.getMessage());

            response.setContentType("application/json");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (ServletException e) {
            String error = "Authorization header is invalid/not found";
            logger.error(error, e);

            ResponseDTO<Object> errorResponse = ResponseDTO.failure(error);

            response.setContentType("application/json");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (Exception e) {
            String error = "user is invalid/not found";
            logger.error(error, e);

            ResponseDTO<Object> errorResponse = ResponseDTO.failure(error);

            response.setContentType("application/json");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return (path.startsWith("/api/v1/users") && !path.equals("/api/v1/users/logout") || path.equals("/api/v1/profiles/payment/webhook") ||  path.equals("/api/v1/health"));
    }

    private Optional<String> readCookie(HttpServletRequest request, String name) {
        if(request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}

