package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.repository.UserRepository;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void checkLogin(String username, String password) {
        Optional<User> user = findUserByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_USER);
        }
        if (!user.get().getPassword().equals(password)) {
            throw new InvalidException(MessageConstants.WRONG_PASS);
        }
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public void resetPassword(String username, String pin, String newPassword, String confirmPassword) {
        Optional<User> user = findUserByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_USER);
        }
        if (!user.get().getPin().equals(pin)) {
            throw new InvalidException("Sai mã pin");
        }
        if (StringUtils.isEmpty(newPassword)) {
            throw new InvalidException("Bạn chưa nhập mật khẩu mới");
        } else if (newPassword.length() > 32 || newPassword.length() < 6) {
            throw new InvalidException("Mật khẩu phải lớn hơn 6 hoặc nhỏ hơn 32 kí tự");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new InvalidException("Xác nhận lại mật khẩu không đúng");
        }
        user.get().setPassword(newPassword);
        user.get().setConfirmPassword(confirmPassword);
        userRepository.save(user.get());
    }

    @Override
    public void checkRoleAdmin(Long idUser) {
        Optional<User> user = findById(idUser);
        if (user.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_USER);
        }
        if (!SalesManagementConstants.ROLE_ADMIN.equals(user.get().getRole())) {
            throw new InvalidException(MessageConstants.NOT_ADMIN);
        }
    }

    @Override
    public void validateUser(User user) {
        if (user.getUsername().length() > 50) {
            throw new InvalidException("User name vượt quá 50 kí tự");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new InvalidException("Xác nhận lại mật khẩu không đúng");
        }
        if (user.getPassword().length() > 32 || user.getPassword().length() < 6) {
            throw new InvalidException("Mật khẩu phải lớn hơn 6 hoặc nhỏ hơn 32 kí tự");
        }
        if (user.getPhone().length() != 10) {
            throw new InvalidException("Số điện thoại chỉ có 10 số thôi");
        }
        if (user.getPin().length() != 8) {
            throw new InvalidException("Số pin chỉ có 8 số");
        }
        user.setStatus(SalesManagementConstants.STATUS_ACTIVE);
        user.setRole(SalesManagementConstants.ROLE_BUYER);
        if (user.getRole().equals(SalesManagementConstants.ROLE_ADMIN)) {
            user.setRole(SalesManagementConstants.ROLE_ADMIN);
        }
        userRepository.save(user);
    }

    @Override
    public void checkBannerUser(String username) {
        Optional<User> user = findUserByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_USER);
        }
        if (SalesManagementConstants.STATUS_USER_BANED.equals(user.get().getStatus())) {
            throw new InvalidException(MessageConstants.USER_HAS_BANED);
        }
    }

    public User checkExistUser(Long idUser) {
        Optional<User> user = findById(idUser);
        if (user.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_USER);
        } else {
            return user.get();
        }
    }

    @Override
    public void banUser(Long idAdmin, Long idUser) {
        User admin = checkExistUser(idAdmin);
        if (!SalesManagementConstants.ROLE_ADMIN.equals(admin.getRole())) {
            throw new InvalidException(MessageConstants.NOT_ADMIN);
        }
        User user = checkExistUser(idUser);
        user.setStatus(SalesManagementConstants.STATUS_USER_BANED);
        userRepository.save(user);
    }

    @Override
    public void unbanUser(Long idAdmin, Long idUser) {
        User admin = checkExistUser(idAdmin);
        if (!SalesManagementConstants.ROLE_ADMIN.equals(admin.getRole())) {
            throw new InvalidException(MessageConstants.NOT_ADMIN);
        }
        User user = checkExistUser(idUser);
        user.setStatus(SalesManagementConstants.STATUS_ACTIVE);
        userRepository.save(user);
    }
}
