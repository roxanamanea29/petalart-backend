package com.example.login_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

   @ManyToMany(mappedBy = "address")
    private List<Order> orders;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String phoneNumber;
    private String addressType; // Puede ser "billing" o "shipping"
}
