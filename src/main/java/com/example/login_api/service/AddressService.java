package com.example.login_api.service;

import com.example.login_api.dto.AddressRequest;
import com.example.login_api.dto.AddressResponse;
import com.example.login_api.entity.Address;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.enums.AddressType;
import com.example.login_api.repository.IAddressRepository;
import com.example.login_api.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor

public class AddressService {

    private final IAddressRepository addressRepository;
    private final IUserRepository   userRepository;


    public AddressResponse saveAddress(AddressRequest request, Long userId) {
        // Validar el usuario
        UserEntity  user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        Address addressEntity = new Address();
        addressEntity.setUser(user);
        addressEntity.setStreet(request.getStreet());
        addressEntity.setStreetNumber(request.getStreetNumber());
        addressEntity.setCity(request.getCity());
        addressEntity.setState(request.getState());
        addressEntity.setCountry(request.getCountry());
        addressEntity.setZipCode(request.getZipCode());
        addressEntity.setAddressType(AddressType.valueOf(request.getAddressType()));

        // Save the address entity to the database
        Address savedAddress = addressRepository.save(addressEntity);
        // Create a response object to return
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setId(savedAddress.getId());
        addressResponse.setStreet(savedAddress.getStreet());
        addressResponse.setStreetNumber(savedAddress.getStreetNumber());
        addressResponse.setCity(savedAddress.getCity());
        addressResponse.setState(savedAddress.getState());
        addressResponse.setCountry(savedAddress.getCountry());
        addressResponse.setZipCode(savedAddress.getZipCode());
        addressResponse.setAddressType(savedAddress.getAddressType());

      return mapToAddressResponse(savedAddress);
    }


    public AddressResponse updateAddress(Long id, AddressRequest request) {
        Address addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));

        addressEntity.setStreet(request.getStreet());
        addressEntity.setStreetNumber(request.getStreetNumber());
        addressEntity.setCity(request.getCity());
        addressEntity.setState(request.getState());
        addressEntity.setCountry(request.getCountry());
        addressEntity.setZipCode(request.getZipCode());
        addressEntity.setAddressType(AddressType.valueOf(request.getAddressType()));

        // Save the updated address entity to the database
        Address updatedAddress = addressRepository.save(addressEntity);
        // Create a response object to return
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setId(updatedAddress.getId());
        addressResponse.setStreet(updatedAddress.getStreet());
        addressResponse.setStreetNumber(updatedAddress.getStreetNumber());
        addressResponse.setCity(updatedAddress.getCity());
        addressResponse.setState(updatedAddress.getState());
        addressResponse.setCountry(updatedAddress.getCountry());
        addressResponse.setZipCode(updatedAddress.getZipCode());
        addressResponse.setAddressType(updatedAddress.getAddressType());

      return mapToAddressResponse(updatedAddress);
    }
    public void deleteAddress(Long id) {
        Address addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));
        addressRepository.delete(addressEntity);
    }

    private AddressResponse mapToAddressResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setStreet(address.getStreet());
        response.setStreetNumber(address.getStreetNumber());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setCountry(address.getCountry());
        response.setZipCode(address.getZipCode());
        response.setAddressType(address.getAddressType());
        return response;
    }

}
