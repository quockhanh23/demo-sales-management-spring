package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.dto.UserDTO;
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
        try {
            userService.validateUser(user);
            userService.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long idUser, @RequestParam String status) {
        try {
            Optional<User> userOptional = userService.findById(idUser);
            userOptional.ifPresent(user -> user.setStatus(status));
            return new ResponseEntity<>(userOptional, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getInformation")
    public ResponseEntity<?> getInformation(@RequestParam Long idUser) {
        try {
            Optional<User> user = userService.findById(idUser);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            userService.checkLogin(user.getUsername(), user.getPassword());
            userService.checkBannerUser(user.getUsername());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO user) {
        try {
            userService.resetPassword(user.getUsername(), user.getPin(), user.getNewPassword(), user.getConfirmPassword());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
