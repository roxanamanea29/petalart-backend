package com.example.login_api.entity;


import com.example.login_api.enums.AddressType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "order_addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;
}

