package com.example.login_api.enums;

import lombok.Getter;

/**
 * Enum para los tipos de dirección.
 */
@Getter
public enum AddressType {
    // Dirección de envío
    SHIPPING("Dirección de envío"),
    // Dirección de facturación
    BILLING("Dirección de facturación"),
    // la misma diecion sirve para facuracion como para envio
    BOTH("Ambas direcciones");

    private final String displayName;

    AddressType(String displayName) {
        this.displayName = displayName;
    }
}
