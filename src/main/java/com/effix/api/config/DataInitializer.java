package com.effix.api.config;

import com.effix.api.model.entity.UserModel;
import com.effix.api.repository.UserRepository;
import com.effix.api.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;
    private PasswordUtil passwordUtil;

    @Bean
    ApplicationRunner initializeData() {
        return args -> {
//            UserModel user1 = userRepository.findByEmail("user@geteffix.com");
//            if(user1 == null){
//                user1 = UserModel.builder()
//                        .email("user@geteffix.com")
//                        .firstName("User 1")
//                        .lastName("User 1")
//                        .password(PasswordUtil.encrypt("password123"))
//                        .isSuper(true)
//                        .verified(true)
//                        .build();
//
//                userRepository.save(user1);
//            }
//
//            UserModel user2 = userRepository.findByEmail("jayslouis@gmail.com");
//            if(user2 == null){
//                user2 = UserModel.builder()
//                        .firstName("Jay")
//                        .lastName("L")
//                        .email("jayslouis@gmail.com")
//                        .password(PasswordUtil.encrypt("password123"))
//                        .isSuper(true)
//                        .verified(true)
//                        .build();
//
//                userRepository.save(user2);
//            }
        };
    }
}

