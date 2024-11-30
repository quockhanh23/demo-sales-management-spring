package com.example.quanlybanhang.controller;


import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.constant.SalesManagementConstants;
import com.example.quanlybanhang.exeption.InvalidException;
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
            throw new InvalidException(MessageConstants.NOT_ADMIN);
        }
        List<User> userList = userRepository.findAll();
        if (!CollectionUtils.isEmpty(userList)) {
            userList.removeIf(n -> (n.getRole().equals(SalesManagementConstants.ROLE_ADMIN)));
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/user-action")
    public ResponseEntity<?> userAction(@RequestParam Long idAdmin,
                                        @RequestParam Long idUser,
                                        @RequestParam String type) {
        if (SalesManagementConstants.STATUS_ACTIVE.equalsIgnoreCase(type)) {
            userService.unbanUser(idAdmin, idUser);
        }
        if (SalesManagementConstants.STATUS_USER_BANED.equalsIgnoreCase(type)) {
            userService.banUser(idAdmin, idUser);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
