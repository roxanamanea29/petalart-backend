package com.example.login_api.enums;

public enum PaymentStatus {
    PENDING("Pago pendiente"),
    COMPLETED("Pago completado"),
    FAILED("Pago fallado"),
    REFUNDED("Pago reembolsado"),
    CANCELLED("Pago cancelado");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
