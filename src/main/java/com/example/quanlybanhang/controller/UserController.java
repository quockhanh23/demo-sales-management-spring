package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.constant.MessageConstants;
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
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.validateUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@RequestParam Long idUser, @RequestParam String status) {
        try {
            Optional<User> userOptional = userService.findById(idUser);
            userOptional.ifPresent(user -> user.setStatus(status));
            return new ResponseEntity<>(userOptional, HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getInformation")
    public ResponseEntity<?> getInformation(@RequestParam Long idUser) {
        try {
            User user = userService.checkExistUser(idUser);
            user.setPassword(null);
            user.setConfirmPassword(null);
            user.setPin(null);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            userService.checkLogin(user.getUsername(), user.getPassword());
            userService.checkBannerUser(user.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO user) {
        try {
            userService.resetPassword(user.getUsername(), user.getPin(),
                    user.getNewPassword(), user.getConfirmPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findByUserName")
    public ResponseEntity<?> findByUserName(@RequestParam String username) {
        Optional<User> userOptional = userService.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            throw new InvalidException(MessageConstants.NOT_FOUND_USER);
        }
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }
}
