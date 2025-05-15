package com.example.login_api.repository;

import com.example.login_api.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAddressRepository extends JpaRepository<Address, Long> {// JpaRepository is a Spring Data interface that provides CRUD operations for the entity class Address.

   List<Address> findByUserId(Long userId); // metodo para buscar todas las direcciones de un usuario por su id
}
