package com.example.quanlybanhang.controller;


import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.repository.UserRepository;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/get-all-user")
    public ResponseEntity<?> getAllUser(@RequestParam Long idUser) {
        User admin = userService.checkExistUser(idUser);
        if (!SalesManagementConstants.ROLE_ADMIN.equals(admin.getRole())) {
            throw new BadRequestException(MessageConstants.NOT_ADMIN);
        }
        List<User> userList = userRepository.findAll();
        if (!CollectionUtils.isEmpty(userList)) {
            userList.removeIf(n -> (n.getRole().equals(SalesManagementConstants.ROLE_ADMIN)));
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/banner")
    public ResponseEntity<?> banUser(@RequestParam Long idAdmin, @RequestParam Long idUser) {
        User admin = userService.checkExistUser(idAdmin);
        if (!SalesManagementConstants.ROLE_ADMIN.equals(admin.getRole())) {
            throw new BadRequestException(MessageConstants.NOT_ADMIN);
        }
        User user = userService.checkExistUser(idUser);
        user.setStatus(SalesManagementConstants.STATUS_USER_BANED);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/un-ban-user")
    public ResponseEntity<?> unbanUser(@RequestParam Long idAdmin, @RequestParam Long idUser) {
        User admin = userService.checkExistUser(idAdmin);
        if (!SalesManagementConstants.ROLE_ADMIN.equals(admin.getRole())) {
            throw new BadRequestException(MessageConstants.NOT_ADMIN);
        }
        User user = userService.checkExistUser(idUser);
        user.setStatus(SalesManagementConstants.STATUS_ACTIVE);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
