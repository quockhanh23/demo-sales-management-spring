package com.example.quanlybanhang.controller;

import com.example.quanlybanhang.models.Address;
import com.example.quanlybanhang.service.AddressService;
import com.example.quanlybanhang.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final UserService userService;

    private final AddressService addressService;

    @GetMapping("/get-all-address-by-user")
    public ResponseEntity<Object> getAllAddressByUser(@RequestParam Long idUser) {
        userService.checkExistUser(idUser);
        List<Address> addressList = addressService.getAllByIdUser(idUser);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }

    @GetMapping("/create-address")
    public ResponseEntity<Object> createAddress(@RequestParam Long idUser,
                                                @RequestBody Address address) {
        userService.checkExistUser(idUser);
        addressService.createAddress(address);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
