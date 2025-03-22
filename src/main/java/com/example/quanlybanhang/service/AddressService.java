package com.example.quanlybanhang.service;

import com.example.quanlybanhang.models.Address;

import java.util.List;

public interface AddressService {

    void createAddress(Address address);

    void selectAddress(Long idUser, Long idAddress);

    Address getDetailAddress(Long idAddress);

    void deleteAddress(Long idAddress);

    List<Address> getAllByIdUser(Long idUser);
}
