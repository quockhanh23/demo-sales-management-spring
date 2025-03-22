package com.example.quanlybanhang.repository;

import com.example.quanlybanhang.common.AddressStatus;
import com.example.quanlybanhang.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.idUser = :idUser AND a.status = :status")
    List<Address> getAllByIdUser(@Param("idUser") Long idUser, @Param("status") AddressStatus status);
}
