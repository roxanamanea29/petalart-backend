package com.example.login_api.service;


import com.example.login_api.dto.OrderItemResponse;
import com.example.login_api.dto.OrderResponse;
import com.example.login_api.entity.*;
import com.example.login_api.enums.OrderStatus;
import com.example.login_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    //se inyectan los repositorios necesarios para el servicio
    private final IOrderRepository orderRepository;
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IAddressRepository addressRepository;

    public OrderResponse createOrder(Long userId, List<Long> addressIds) {
        //primero se busca el usuario o lanza una excepcion si no existe
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado :("));
        // busca el carrito del usuario
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // se verifica que el carrito no esta vacío
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }
        //
        List<Address> addresses = addressRepository.findByUserId(userId);

        // se crea el pedido
        Order order = new Order();//se crea la instacnia de la clase Order
        order.setUser(user);//se le asigna el usuario
        order.setAddresses(addresses);//se le asigna la lista de direcciones
        order.setDate(LocalDateTime.now());//se le asigna la fecha de creación
        order.setStatus(OrderStatus.PENDING_PAYMENT);//se le asigna el estado pendiente


        //convertir el carrito a una lista de OrderItem
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {//el stream se usa para recorrer la lista de items del carrito y trans
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());//recolecta los items en una lista de tipo List<OrderItem>

        order.setItems(orderItems);
        BigDecimal total = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);//se le asigna el total al pedido

        orderRepository.save(order);//guardar el pedido en la base de datos
        orderItemRepository.saveAll(orderItems);//guardar los items del pedido en la base de datos

        cart.getItems().clear();//limpiar el carrito
        cartRepository.save(cart);//guardar el carrito vacío en la base de datos

        // guardar el pedido
        return mapToOrderResponseDTO(order);
    }

    //mapea un Order a un OrderResponse
    private OrderResponse mapToOrderResponseDTO(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setDate(order.getDate());
        dto.setTotal(order.getTotal());

        List<OrderItemResponse> itemDTOs = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        return dto;
    }

    //método para obtener todos los pedidos de un usuario
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado :("));
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }
    public OrderResponse getOrderById(Long orderId) {
       Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado :("));
        return mapToOrderResponseDTO(order);
    }


    //mapea un OrderItem a un OrderItemResponse
    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        OrderItemResponse dto = new OrderItemResponse();
        dto.setOrderItemId(item.getOrderItemId());
        dto.setProductName(item.getProduct().getProductName());
        dto.setImageUrl(item.getProduct().getProductImage());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}
