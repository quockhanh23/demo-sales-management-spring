package com.example.quanlybanhang.service;

import com.example.quanlybanhang.models.Address;

import java.util.List;

public interface AddressService {

    void createAddress(Address address);

    void deleteAddress(Long idAddress);

    List<Address> getAllByIdUser(Long idUser);
}
