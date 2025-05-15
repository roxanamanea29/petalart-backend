package com.example.login_api.controller;

import com.example.login_api.dto.AddressRequest;
import com.example.login_api.dto.AddressResponse;
import com.example.login_api.security.UserPrincipal;
import com.example.login_api.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    // Constructor injection for AddressService
    private final AddressService addressService;


    // Endpoint to save an address
    @PostMapping("/save")
    public ResponseEntity<AddressResponse>  saveAddress(@RequestBody AddressRequest request,
                                                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long userId = Long.valueOf(userPrincipal.getUserId());
        AddressResponse addressResponse = addressService.saveAddress(request, userId);
        return ResponseEntity.ok(addressResponse);
    }
}
