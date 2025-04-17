package com.example.login_api.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    //relacion con el usuario(solo puede pertenecer a un solo usuario)
    // En Cart:
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)//si se elimina un cartitem se elimnara del cart
    private List<CartItem> items= new ArrayList<>();

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
