package com.example.login_api.service;


import com.example.login_api.dto.AddressResponse;
import com.example.login_api.dto.OrderItemResponse;
import com.example.login_api.dto.OrderRequest;
import com.example.login_api.dto.OrderResponse;
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
    //se inyectan los repositorios necesarios para el servicio
    private final IOrderRepository orderRepository;
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IAddressRepository addressRepository;

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }


    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {

        //validar el usuario -> primero se busca el usuario o lanza una excepcion si no existe
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado :("));

        //validar el carrito -> busca el carrito del usuario
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        // se verifica que el carrito no esta vacío
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }
        //creación del pedido
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PAID);//se asigna el estado del pedido
        order.setPaymentStatus(PaymentStatus.PENDING); //se asigna el estado del pago
        order.setDate(LocalDateTime.now());
        order.setPaymentMethod(orderRequest.getPaymentMethod());//se asigna el método de pago
        order.setShippingMethod(orderRequest.getShippingMethod());//se asigna el método de envíoaho

        //se asignan las direcciones del pedido
        List<OrderAddress> orderAddresses = addressRepository.findAllById(orderRequest.getAddressIds())
                .stream()
                .map(address -> {
                    OrderAddress orderAddress = new OrderAddress();
                    orderAddress.setOrder(order);
                    orderAddress.setAddress(address);
                    orderAddress.setAddressType(orderRequest.getAddressType());//se asigna el tipo de dirección
                    return orderAddress;
                }).collect(Collectors.toList());
        order.setOrderAddresses(orderAddresses);

        //convertir el carrito a pedido
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {//el stream se usa para recorrer la lista de items del carrito y trans
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());//recolecta los items en una lista de tipo List<OrderItem>
        order.setItems(orderItems);

        //calcular el total del pedido
        BigDecimal total = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);//se le asigna el total al pedido

        //guardar ttodo en la base de datos
        orderRepository.save(order);//guardar el pedido en la base de datos
        orderItemRepository.saveAll(orderItems);//guardar los items del pedido en la base de datos
        //limpiar el carrito
        cart.getItems().clear();
        //guardar el carrito vacío en la base de datos
        cartRepository.save(cart);

        // guardar el pedido
        return mapToOrderResponseDTO(order);
    }

    //mapea un Order a un OrderResponse
    private OrderResponse mapToOrderResponseDTO(Order order) {

        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setDate(order.getDate());
        dto.setTotal(order.getTotal());
        dto.setUserId(order.getUser().getId());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setPaymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().name() : null);
        dto.setShippingMethod(order.getShippingMethod() != null ? order.getShippingMethod().name() : null);


        //mapealos items del pedido
        List<OrderItemResponse> itemDTOs = order.getItems().stream()
                .map(this::mapToOrderItemResponse)
                .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        //mapea las direcciones del pedido OrderAddresss-<>Address ->addresseResponse
        if (order.getOrderAddresses() != null) {
            List<AddressResponse> addressDTOs = order.getOrderAddresses().stream()
                    .map(orderAddress -> {
                        Address address = orderAddress.getAddress();//se obtiene la dirección del pedido
                        //mapea la dirección a un AddressResponse
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
                    })
                    .collect(Collectors.toList());
            dto.setAddresses(addressDTOs);
        }
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

    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado :("));
            if (order.getOrderStatus() == null ||
                order.getPaymentStatus() == null ||
                order.getPaymentMethod() == null ||
                order.getShippingMethod() == null
        ) {throw new RuntimeException("No se puede eliminar una orden incompleto (campos nulos)");}

    if(order.getOrderStatus() == OrderStatus.PAID ||
            order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("No se puede eliminar un pedido que no está pendiente");
        }
        orderRepository.delete(order);
    }
}
