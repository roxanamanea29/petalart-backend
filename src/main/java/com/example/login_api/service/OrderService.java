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

/*
* clase de servicio que maneja la lógica de negocio de los pedidos
* se encarga de crear, obtener, actualizar y eliminar pedidos
* */

// se usa la anotación @Service para indicar que es un servicio de Spring y se usa Lombok para generar el constructor con los repositorios inyectados
@Service
@RequiredArgsConstructor
public class OrderService {

    //se inyectan los repositorios necesarios para el servicio
    private final IOrderRepository orderRepository;
    private final ICartRepository cartRepository;
    private final IUserRepository userRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IAddressRepository addressRepository;


    //método para obtener todos los pedidos
    public List<OrderResponse> getAllOrders() {
        //se obtiene la lista de pedidos de la base de datos
        List<Order> orders = orderRepository.findAll();
        //se mapea cada pedido a un OrderResponse y se añade en una lista
        return orders.stream()
                //mapea cada Order al metodo un toOrderResponse
                .map(this::mapToOrderResponseDTO)
                //recolecta los OrderResponse en una lista
                .collect(Collectors.toList());
    }


    //método para crear un pedido
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
        List<OrderAddress> orderAddresses = orderRequest.getAddresses().stream().map(addressRequest -> {
            Address address = new Address();
            address.setStreet(addressRequest.getStreet());
            address.setStreetNumber(addressRequest.getStreetNumber());
            address.setCity(addressRequest.getCity());
            address.setState(addressRequest.getState());
            address.setCountry(addressRequest.getCountry());
            address.setZipCode(addressRequest.getZipCode());
            address.setAddressType(addressRequest.getAddressType());
            address.setUser(user); // se asigna el usuario a la dirección
            addressRepository.save(address); // se guarda la dirección en la base de datos

            OrderAddress orderAddress = new OrderAddress();
            orderAddress.setAddress(address); // se asigna la dirección al pedido
            orderAddress.setAddressType(addressRequest.getAddressType()); // se asigna el tipo de dirección
            orderAddress.setOrder(order); // se asigna el pedido a la dirección
            return orderAddress; // se devuelve el OrderAddress con la dirección y el tipo de dirección

        }).collect(Collectors.toList());//recolecta las direcciones en una lista de tipo List<OrderAddress>

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
        // se crea un nuevo OrderItemResponse para devolver los datos del item del pedido
        OrderItemResponse dto = new OrderItemResponse();
        dto.setOrderItemId(item.getOrderItemId());
        dto.setProductName(item.getProduct().getProductName());
        dto.setImageUrl(item.getProduct().getProductImage());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        // se devuelve el OrderItemResponse con los datos del item del pedido
        return dto;
    }

    //método para eliminar un pedido
    public void deleteOrder(Long orderId) {
    // se busca el pedido por id, si no existe lanza una excepción
        Order order = orderRepository.findById(orderId)
            // si no lo encuentra lanza una excepción
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado :("));
    //verifica que el pedido no tengo campos vacios
            if (order.getOrderStatus() == null ||
                order.getPaymentStatus() == null ||
                order.getPaymentMethod() == null ||
                order.getShippingMethod() == null
        ) {throw new RuntimeException("No se puede eliminar una orden incompleto (campos nulos)");}
//verifica que el pedido no esta en estos estsdos si lo estan
    if(order.getOrderStatus() == OrderStatus.PAID ||
            order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("No se puede eliminar un pedido que no está pendiente");
        }
    //si el pedido no tiene campos nulos y no esta en estos estado se elimna
        orderRepository.delete(order);
    }
}
