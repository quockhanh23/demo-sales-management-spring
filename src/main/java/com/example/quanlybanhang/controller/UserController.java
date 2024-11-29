package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.dto.UserDTO;
import com.example.quanlybanhang.exeption.BadRequestException;
import com.example.quanlybanhang.models.User;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userService.validateUser(user);
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long idUser, @RequestParam String status) {
        Optional<User> userOptional = userService.findById(idUser);
        userOptional.ifPresent(user -> user.setStatus(status));
        return new ResponseEntity<>(userOptional, HttpStatus.OK);
    }

    @GetMapping("/getInformation")
    public ResponseEntity<?> getInformation(@RequestParam Long idUser) {
        Optional<User> userOptional = userService.findById(idUser);
        if (userOptional.isEmpty()) {
            throw new BadRequestException(MessageConstants.NOT_FOUND_USER);
        }
        userOptional.get().setPassword(null);
        userOptional.get().setConfirmPassword(null);
        userOptional.get().setPin(null);
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        userService.checkLogin(user.getUsername(), user.getPassword());
        userService.checkBannerUser(user.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO user) {
        userService.resetPassword(user.getUsername(), user.getPin(),
                user.getNewPassword(), user.getConfirmPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findByUserName")
    public ResponseEntity<?> findByUserName(@RequestParam String username) {
        Optional<User> userOptional = userService.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            throw new BadRequestException(MessageConstants.NOT_FOUND_USER);
        }
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }
}
