package com.example.login_api.config;



import com.example.login_api.dto.CartItemResponse;
import com.example.login_api.entity.CartItem;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // CORRECTO: addMappings recibe un PropertyMap que tiene acceso a map()
        mapper.typeMap(CartItem.class, CartItemResponse.class).addMappings(mapperConfig -> {
            mapperConfig.map(src -> src.getProduct().getProductId(), CartItemResponse::setProductId);
            mapperConfig.map(src -> src.getProduct().getProductName(), CartItemResponse::setProductName);
            mapperConfig.map(src -> src.getProduct().getPrice(), CartItemResponse::setPrice);
        });

        return mapper;
    }
}
