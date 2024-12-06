package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.models.Address;
import com.example.quanlybanhang.repository.AddressRepository;
import com.example.quanlybanhang.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public void createAddress(Address address) {
        addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long idAddress) {

    }

    @Override
    public List<Address> getAllByIdUser(Long idUser) {
        List<Address> addressList = addressRepository.getAllByIdUser(idUser);
        if (CollectionUtils.isEmpty(addressList)) addressList = new ArrayList<>();
        return addressList;
    }
}
