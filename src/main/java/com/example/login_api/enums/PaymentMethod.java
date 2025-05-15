package com.example.login_api.enums;

public enum PaymentMethod {
    CREDIT_CARD("Tarjeta de crédito"),
    PAYPAL("PayPal"),
    CASH_ON_DELIVERY("Efectivo a la entrega");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
