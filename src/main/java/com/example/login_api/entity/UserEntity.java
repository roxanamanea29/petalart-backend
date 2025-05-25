package com.example.login_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * UserEntity clase que representa la entidad de usuario en la base de datos.
 * It contains user details such as first name, last name, email, password, phone number, and role.
 * It also establishes relationships with Cart, Order, and Payment entities.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")} )
public class UserEntity {
    // atributos de la clase


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// id del usuario

    @Column(name = "first_name", nullable = false)
    private String firstName;// nombre del usuario

    @Column(name = "last_name", nullable = false)
    private String lastName;// apellido del usuario

    @Column(unique = true, nullable = false)
    private String email;// email del usuario

    @JsonIgnore
    @Column(nullable = false)
    private String password;// contraseña del usuario

    private String phone;// telefono del usuario

    //role
    @Column(nullable = false)
    private String role;// rol del usuario

    //relation con Cart
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cart> carts;// lista de carritos del usuario

    //relation con Order
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Order> orders;// lista de pedidos del usuario

    //relation con Payment
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Payment> payments;// lista de pagos del usuario

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;// fecha de creación del usuario

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;// fecha de actualización del usuario

}
