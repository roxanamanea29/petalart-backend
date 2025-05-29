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


    //guarda la direccion del usuario
    public AddressResponse saveAddress(AddressRequest request, Long userId) {
        // Validar el usuario sino lanza una excepcion
        UserEntity  user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    //busca por si existe direccion con los mismos datos
        Optional<Address> existingAddress = addressRepository.findByUserAndStreetAndStreetNumberAndCityAndZipCodeAndCountry(
                user,
                request.getStreet(),
                request.getStreetNumber(),
                request.getCity(),
                request.getZipCode(),
                request.getCountry()
        );
        //crea una direción nuevo o modifica la existente
        Address addressEntity;
        // busca si la dirección ya existe para el usuario
        if (existingAddress.isPresent()) {
            // si existe, se actualiza la dirección
            addressEntity = existingAddress.get();
            addressEntity.setAddressType(request.getAddressType());
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
            addressEntity.setAddressType(request.getAddressType());
        }
        // Save the address entity to the database
        Address savedAddress = addressRepository.save(addressEntity);
      return mapToAddressResponse(savedAddress);
    }


    //actualiza la dieccion utilizndo el id de la direcion y el id del usuario
    public AddressResponse updateAddress(Long id, AddressRequest request, Long userId) {
        //verifica si el usuario existe sino lanza una excepcion
        Address addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + id));
        //si la direcion no le pertnece al ususrio da error
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
        addressEntity.setAddressType(request.getAddressType());

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

    //metodo para mapear la direcion e
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

        //datos del usuario
        UserEntity addressUser = address.getUser();
        response.setUserId(addressUser.getId());
        response.setUserEmail(address.getUser().getEmail());
        response.setUserName(address.getUser().getFirstName() + " " + address.getUser().getLastName());
        return response;

    }

}
