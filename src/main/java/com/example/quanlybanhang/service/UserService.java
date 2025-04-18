package com.example.quanlybanhang.service;

import com.example.quanlybanhang.dto.ResetPassword;
import com.example.quanlybanhang.dto.UserDTO;
import com.example.quanlybanhang.models.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    void delete(Long id);

    void checkLogin(String username, String password);

    Optional<User> findUserByUsername(String username);

    User resetPassword(String username, String pin);

    void changePassword(ResetPassword resetPassword, Long idUser);

    UserDTO updateInformation(UserDTO userDTO, Long idUser);

    void checkRoleAdmin(Long idUser);

    void validateUser(User user);

    void checkBannerUser(String username);

    User checkExistUser(Long idUser);

    void banUser(Long idAdmin, Long idUser);

    void unbanUser(Long idAdmin, Long idUser);
}
