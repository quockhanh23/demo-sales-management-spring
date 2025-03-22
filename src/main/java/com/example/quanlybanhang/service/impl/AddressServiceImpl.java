package com.example.quanlybanhang.service.impl;

import com.example.quanlybanhang.common.AddressStatus;
import com.example.quanlybanhang.exeption.InvalidException;
import com.example.quanlybanhang.models.Address;
import com.example.quanlybanhang.repository.AddressRepository;
import com.example.quanlybanhang.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public void createAddress(Address address) {
        address.setStatus(AddressStatus.ACTIVE);
        addressRepository.save(address);
    }

    @Override
    public void selectAddress(Long idUser, Long idAddress) {
        List<Address> addressList = getAllByIdUser(idUser);
        if (!CollectionUtils.isEmpty(addressList)) {
            addressList.forEach(address1 -> address1.setInUse(false));
        }
        Address address = getDetailAddress(idAddress);
        address.setInUse(true);
        address.setUpdatedAt(new Date());
        addressList.add(address);
        addressRepository.saveAll(addressList);
    }

    @Override
    public Address getDetailAddress(Long idAddress) {
        Optional<Address> addressOptional = addressRepository.findById(idAddress);
        if (addressOptional.isEmpty()) {
            throw new InvalidException("Not found");
        } else {
            return addressOptional.get();
        }
    }

    @Override
    public void deleteAddress(Long idAddress) {
        Address address = getDetailAddress(idAddress);
        address.setStatus(AddressStatus.INACTIVE);
        addressRepository.save(address);
    }

    @Override
    public List<Address> getAllByIdUser(Long idUser) {
        List<Address> addressList = addressRepository.getAllByIdUserAndStatus(idUser, AddressStatus.ACTIVE);
        if (CollectionUtils.isEmpty(addressList)) addressList = new ArrayList<>();
        return addressList;
    }
}
