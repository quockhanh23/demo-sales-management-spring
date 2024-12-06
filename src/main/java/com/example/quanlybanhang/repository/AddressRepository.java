package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> getAllByIdUser(Long idUser);
}
