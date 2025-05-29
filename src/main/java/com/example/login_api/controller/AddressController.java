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

    // metodo utilizado para obtener todas las direcciones
    @GetMapping("/listado")
    public ResponseEntity<List<AddressResponse>> getAllAddresses() {
        List<AddressResponse> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }
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
    public ResponseEntity<String>  deleteAddress(@PathVariable Long addressId,
                                                 @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = userPrincipal.getUserId();
        addressService.deleteAddress(addressId, userId);
        return ResponseEntity.ok("Address deleted successfully");

    }
}
