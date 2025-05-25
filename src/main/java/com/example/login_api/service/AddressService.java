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

import java.util.List;
import java.util.Optional;

/**
 * AddressService es una clase de servicio que maneja la lógica de negocio relacionada con las direcciones.
 *
 */

@Service
@RequiredArgsConstructor

public class AddressService {
    //dependencias
    private final IAddressRepository addressRepository;
    private final IUserRepository   userRepository;


    //metodo para obtener todas las direcciones
    public List<AddressResponse> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(this::mapToAddressResponse)
                .toList();
    }
    //metodo para obtener todas las direcciones de un usuario
    public List<AddressResponse> getAddressesByUserId(Long userId) {
        // Validar el usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        List<Address> addresses = addressRepository.findByUser(user);
        return addresses.stream()
                .map(this::mapToAddressResponse)
                .toList();
    }


    public AddressResponse saveAddress(AddressRequest request, Long userId) {
        // Validar el usuario
        UserEntity  user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        Optional<Address> existingAddress = addressRepository.findByUserAndStreetAndStreetNumberAndCityAndZipCodeAndCountry(
                user,
                request.getStreet(),
                request.getStreetNumber(),
                request.getCity(),
                request.getZipCode(),
                request.getCountry()
        );
        Address addressEntity;
        // busca si la dirección ya existe para el usuario
        if (existingAddress.isPresent()) {
            // si existe, se actualiza la dirección
            addressEntity = existingAddress.get();
            addressEntity.setAddressType(AddressType.valueOf(request.getAddressType()));
        } else {
            //si no existe se añade una nueva dirección
            addressEntity = new Address();
            addressEntity.setUser(user);
            addressEntity.setStreet(request.getStreet());
            addressEntity.setStreetNumber(request.getStreetNumber());
            addressEntity.setCity(request.getCity());
            addressEntity.setState(request.getState());
            addressEntity.setCountry(request.getCountry());
            addressEntity.setZipCode(request.getZipCode());
            addressEntity.setAddressType(AddressType.valueOf(request.getAddressType()));
        }
        // Save the address entity to the database
        Address savedAddress = addressRepository.save(addressEntity);
      return mapToAddressResponse(savedAddress);
    }



    public AddressResponse updateAddress(Long id, AddressRequest request, Long userId) {
        Address addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));
        if(!addressEntity.getUser().getId().equals(request.getUserId())) {
            throw new RuntimeException("Address does not belong to the user with ID: " + request.getUserId());
        }

         //actualizar la dirección
        addressEntity.setStreet(request.getStreet());
        addressEntity.setStreetNumber(request.getStreetNumber());
        addressEntity.setCity(request.getCity());
        addressEntity.setState(request.getState());
        addressEntity.setCountry(request.getCountry());
        addressEntity.setZipCode(request.getZipCode());
        addressEntity.setAddressType(AddressType.valueOf(request.getAddressType()));

        // Save the updated address entity to the database
        Address updatedAddress = addressRepository.save(addressEntity);
        // Create a response object to retur
      return mapToAddressResponse(updatedAddress);
    }

    // Eliminar una dirección por ID
    public void deleteAddress(Long addressId, Long UserId) {
        Address addressEntity = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("No se ha encotrado una dorecciòn con este id: " + addressId));
        // Verificar si la dirección pertenece al usuario
        if (!addressEntity.getUser().getId().equals(UserId)) {
            throw new RuntimeException("Esta dirección no es tuya : " + UserId);
        }
        addressRepository.delete(addressEntity);
    }

    public AddressResponse mapToAddressResponse(Address address) {
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
