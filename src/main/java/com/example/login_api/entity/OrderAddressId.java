package com.example.login_api.entity;


import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data

public class OrderAddressId implements Serializable {
    private Long orderId;
    private Long addressId;

    public OrderAddressId() {
    }

    public OrderAddressId(Long orderId, Long addressId) {
        this.orderId = orderId;
        this.addressId = addressId;
    }
}
