package com.example.login_api.service;

import com.example.login_api.dto.*;
import com.example.login_api.entity.*;
import com.example.login_api.enums.OrderStatus;
import com.example.login_api.enums.PaymentStatus;
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

    private final IOrderRepository orderRepository;
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IAddressRepository addressRepository;
    private final IProductRepository productRepository;

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {

        // Verifica que el usuario exista
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado :("));

        // Verifica que el carrito del usuario tenga productos
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new RuntimeException("No se enviaron productos para el pedido");
        }
        // Verifica que se hayan proporcionado direcciones y tipo de dirección
        if (orderRequest.getAddressIds() == null || orderRequest.getAddressIds().isEmpty()) {
            throw new RuntimeException("Debes proporcionar al menos una dirección para el pedido");
        }
        // Verifica que se haya especificado el tipo de dirección
        if (orderRequest.getAddressType() == null) {
            throw new RuntimeException("Tipo de dirección no especificado");
        }
        // se crea el pedido
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setDate(LocalDateTime.now());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setShippingMethod(orderRequest.getShippingMethod());


        List<Address> addresses = addressRepository.findAllById(orderRequest.getAddressIds());
        List<OrderAddress> orderAddresses = addresses.stream().map(address -> {
            OrderAddress orderAddress = new OrderAddress();
            orderAddress.setAddress(address);
            orderAddress.setAddressType(orderRequest.getAddressType());
            orderAddress.setOrder(order);
            return orderAddress;
        }).collect(Collectors.toList());
        order.setOrderAddresses(orderAddresses);

        List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto con ID " + itemDto.getProductId() + " no encontrado"));
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPrice()); // Precio del backend por seguridad
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());
        order.setItems(orderItems);

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // limpia el carrito si lo deseas
        cartRepository.findByUser(user).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
        return mapToOrderResponseDTO(order);
    }

    public OrderResponse confirmPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado :("));
        if (order.getOrderStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new RuntimeException("El pedido no está pendiente de pago");
        }
        order.setOrderStatus(OrderStatus.PAID);
        order.setPaymentStatus(PaymentStatus.COMPLETED);
        orderRepository.save(order);
         return mapToOrderResponseDTO(order);
    }


    private OrderResponse mapToOrderResponseDTO(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setUserEmail(order.getUser().getEmail());
        dto.setDate(order.getDate());
        dto.setTotal(order.getTotal());
        dto.setUserId(order.getUser().getId());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().name() : null);
        dto.setShippingMethod(order.getShippingMethod() != null ? order.getShippingMethod().name() : null);

        List<OrderItemResponse> itemDTOs = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        if (order.getOrderAddresses() != null) {
            List<AddressResponse> addressDTOs = order.getOrderAddresses().stream()
                    .map(orderAddress -> {
                        Address address = orderAddress.getAddress();
                        AddressResponse addressDto = new AddressResponse();
                        addressDto.setId(address.getId());
                        addressDto.setStreet(address.getStreet());
                        addressDto.setStreetNumber(address.getStreetNumber());
                        addressDto.setCity(address.getCity());
                        addressDto.setState(address.getState());
                        addressDto.setCountry(address.getCountry());
                        addressDto.setZipCode(address.getZipCode());
                        addressDto.setAddressType(orderAddress.getAddressType());
                        return addressDto;
                    }).collect(Collectors.toList());
            dto.setAddresses(addressDTOs);
        }

        return dto;
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
        OrderItemResponse dto = new OrderItemResponse();
        dto.setOrderItemId(item.getOrderItemId());
        dto.setProductName(item.getProduct().getProductName());
        dto.setImageUrl(item.getProduct().getProductImage());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado :("));
        return orderRepository.findByUser(user).stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado :("));
        return mapToOrderResponseDTO(order);
    }

    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado :("));
        if (order.getOrderStatus() == null || order.getPaymentStatus() == null ||
                order.getPaymentMethod() == null || order.getShippingMethod() == null) {
            throw new RuntimeException("No se puede eliminar una orden incompleta (campos nulos)");
        }
        if (order.getOrderStatus() == OrderStatus.PAID ||
                order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("No se puede eliminar un pedido que no está pendiente");
        }
        orderRepository.delete(order);
    }
}