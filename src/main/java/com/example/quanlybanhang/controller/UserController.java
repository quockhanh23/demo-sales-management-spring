package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.constant.MessageConstants;
import com.example.quanlybanhang.dto.ResetPassword;
import com.example.quanlybanhang.dto.UserDTO;
import com.example.quanlybanhang.exeption.InvalidException;
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
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        userService.validateUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/changeStatus")
    public ResponseEntity<Object> changeStatus(@RequestParam Long idUser, @RequestParam String status) {
        Optional<User> userOptional = userService.findById(idUser);
        userOptional.ifPresent(user -> user.setStatus(status));
        return new ResponseEntity<>(userOptional, HttpStatus.OK);
    }

    @GetMapping("/getInformation")
    public ResponseEntity<Object> getInformation(@RequestParam Long idUser) {
        User user = userService.checkExistUser(idUser);
        user.setPassword(null);
        user.setConfirmPassword(null);
        user.setPin(null);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        userService.checkLogin(user.getUsername(), user.getPassword());
        userService.checkBannerUser(user.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPassword user) {
        return new ResponseEntity<>(userService.resetPassword(user.getUsername(), user.getPin()), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ResetPassword resetPassword,
                                                 @RequestParam Long idUser) {
        userService.changePassword(resetPassword, idUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-information")
    public ResponseEntity<Object> updateInformation(@RequestBody UserDTO userDTO,
                                                    @RequestParam Long idUser) {
        UserDTO user = userService.updateInformation(userDTO, idUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/findByUserName")
    public ResponseEntity<Object> findByUserName(@RequestParam String username) {
        Optional<User> userOptional = userService.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_USER);
        }
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }
}
