package com.example.productservice;


import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {
    private final ProductService productService ;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JacksonProperties jacksonProperties;

    @KafkaListener(topics = "order_created")

     public void handleOrderCreatedEvent ( String orderString) throws Exception{

      /*  OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(orderString , OrderCreatedEvent.class);
        log.info("Receive order message");
        if (orderCreatedEvent != null){
            throw new RuntimeException("customize exception");
        }*/
    }



}

