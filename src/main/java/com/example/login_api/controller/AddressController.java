package com.example.login_api.controller;

import com.example.login_api.dto.AddressRequest;
import com.example.login_api.dto.AddressResponse;
import com.example.login_api.entity.Address;
import com.example.login_api.repository.IAddressRepository;
import com.example.login_api.security.UserPrincipal;
import com.example.login_api.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    // Constructor injection for AddressService
    private final AddressService addressService;
    private final IAddressRepository addressRepository;

    // metodo para obtener todas las direcciones de un usuario
    @GetMapping("/my-addresses")
    public ResponseEntity<List<AddressResponse>> getAddressesByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = Long.valueOf(userPrincipal.getUserId());
        List<AddressResponse> addresses = addressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }


    // metodo para añadir una nueva direccion
    @PostMapping("/save")
    public ResponseEntity<AddressResponse>  saveAddress(@RequestBody AddressRequest request,
                                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = Long.valueOf(userPrincipal.getUserId());
        AddressResponse addressResponse = addressService.saveAddress(request, userId);
        return ResponseEntity.ok(addressResponse);
    }
    // metodo para actualizar una direccion
    @PutMapping("/update/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long addressId,
                                                         @RequestBody AddressRequest request,
                                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        AddressResponse addressResponse = addressService.updateAddress(addressId, request, userId);
        return ResponseEntity.ok(addressResponse);
    }

    // metodo para eliminar una direccion
    @DeleteMapping("/delete/{addressId}")
    public void  deleteAddress( Long addressId, Long userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + addressId));
        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Address does not belong to the user with ID: " + userId);
        }
        addressRepository.deleteById(addressId);
    }

}
