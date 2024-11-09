package com.example.quanlybanhang.service;

import com.example.quanlybanhang.models.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    void save(User user);

    void delete(Long id);

    void checkLogin(String username, String password);

    Optional<User> findUserByUsername(String username);

    void resetPassword(String username, String pin, String newPassword, String confirmPassword);

    void checkRoleAdmin(Long idUser);

    void validateUser(User user);

    void checkBannerUser(String username);

    User checkExistUser(Long idUser);
}
