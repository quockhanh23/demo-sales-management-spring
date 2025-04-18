package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.dto.ResetPassword;
import com.example.quanlybanhang.dto.UserDTO;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.repository.UserRepository;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public User resetPassword(String username, String pin) {
        Optional<User> user = findUserByUsername(username);
        if (user.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_USER);
        }
        if (!user.get().getPin().equals(pin)) {
            throw new InvalidException("Sai mã pin");
        }
        User userResponse = new User();
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        userResponse.setPassword(newPassword);
        user.get().setPassword(newPassword);
        user.get().setConfirmPassword(newPassword);
        user.get().setUpdatedAt(new Date());
        userRepository.save(user.get());
        return userResponse;
    }

    @Override
    public void changePassword(ResetPassword resetPassword, Long idUser) {
        User user = checkExistUser(idUser);
        if (StringUtils.isEmpty(resetPassword.getNewPassword())) {
            throw new InvalidException("Bạn chưa nhập mật khẩu mới");
        } else if (resetPassword.getNewPassword().length() > 32 || resetPassword.getNewPassword().length() < 6) {
            throw new InvalidException("Mật khẩu phải lớn hơn 6 hoặc nhỏ hơn 32 kí tự");
        }
        if (!resetPassword.getNewPassword().equals(resetPassword.getConfirmPassword())) {
            throw new InvalidException("Xác nhận lại mật khẩu không đúng");
        }
        user.setPassword(resetPassword.getNewPassword());
        user.setConfirmPassword(resetPassword.getConfirmPassword());
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    @Override
    public UserDTO updateInformation(UserDTO userUpdate, Long idUser) {
        User user = checkExistUser(idUser);
        if (userUpdate.getPhone().length() != 10) {
            throw new InvalidException("Số điện thoại chỉ có 10 số thôi");
        }
        if (!StringUtils.isEmpty(userUpdate.getFullName()) && userUpdate.getFullName().length() > 100) {
            throw new InvalidException("Họ tên vượt quá 100 kí tự");
        }
        if (StringUtils.isNotEmpty(userUpdate.getFullName())) {
            user.setFullName(userUpdate.getFullName());
        }
        if (StringUtils.isNotEmpty(userUpdate.getPhone())) {
            user.setPhone(userUpdate.getPhone());
        }
        user.setUpdatedAt(new Date());
        user = userRepository.save(user);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
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
        if (StringUtils.isEmpty(user.getUsername())) {
            throw new InvalidException("Bạn chưa điền tên đăng nhập");
        }
        if (!StringUtils.isEmpty(user.getUsername()) && user.getUsername().length() > 50) {
            throw new InvalidException("Tên đăng nhập vượt quá 50 kí tự");
        }
        Optional<User> userOptional = findUserByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            throw new InvalidException("Tên đăng nhập đã tồn tại");
        }
        if (user.getPassword().length() > 32 || user.getPassword().length() < 6) {
            throw new InvalidException("Mật khẩu phải lớn hơn 6 hoặc nhỏ hơn 32 kí tự");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new InvalidException("Xác nhận lại mật khẩu không đúng");
        }
        if (user.getPhone().length() != 10) {
            throw new InvalidException("Số điện thoại chỉ có 10 số thôi");
        }
        if (user.getPin().length() != 8) {
            throw new InvalidException("Số pin chỉ có 8 số");
        }
        if (StringUtils.isEmpty(user.getFullName())) {
            throw new InvalidException("Bạn chưa nhập họ tên đầy đủ");
        }
        if (StringUtils.isNotEmpty(user.getFullName()) && user.getFullName().length() > 100) {
            throw new InvalidException("Họ tên vượt quá 100 kí tự");
        }
        user.setStatus(SalesManagementConstants.STATUS_ACTIVE);
        user.setRole(SalesManagementConstants.ROLE_BUYER);
        if (user.getRole().equals(SalesManagementConstants.ROLE_ADMIN)) {
            user.setRole(SalesManagementConstants.ROLE_ADMIN);
        }
        user.setCreatedAt(new Date());
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
