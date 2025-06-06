package com.example.login_api.dto;


import com.example.login_api.enums.AddressType;
import com.example.login_api.enums.PaymentMethod;
import com.example.login_api.enums.PaymentStatus;
import com.example.login_api.enums.ShippingMethod;
import lombok.Data;

import java.math.BigDecimal;

import java.util.List;

@Data
public class OrderRequest {

    private List<Long> addressIds;

    private List<OrderItemRequest> items;
    private BigDecimal total;

    private PaymentMethod paymentMethod;
    private AddressType addressType;
    private ShippingMethod shippingMethod;
    private PaymentStatus paymentStatus;
}
