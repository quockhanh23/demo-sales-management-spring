package com.example.quanlybanhang;

import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Optional;

@SpringBootApplication
public class QuanlybanhangApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(QuanlybanhangApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public CommandLineRunner run(UserRepository userRepository) {
        return args ->
        {
            Optional<User> adminOptional = userRepository.findUserByUsername("admin");
            if (adminOptional.isEmpty()) {
                User admin = new User();
                admin.setStatus(SalesManagementConstants.STATUS_ACTIVE);
                admin.setRole(SalesManagementConstants.ROLE_ADMIN);
                admin.setUsername("admin");
                admin.setPassword("admin");
                admin.setConfirmPassword("admin");
                admin.setPin("12345678");
                admin.setCreatedAt(new Date());
                admin.setUpdatedAt(new Date());
                userRepository.save(admin);
            }
        };
    }
}
