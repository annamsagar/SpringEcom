package com.livein.springecom.controller;

import com.livein.springecom.model.dto.OrderRequest;
import com.livein.springecom.model.dto.OrderResponse;
import com.livein.springecom.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin()
public class OrderController {

    @Autowired
    private OrderService orderservice;

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestBody OrderRequest orderRequest) {

        OrderResponse orderResponse = orderservice.placeOrder(orderRequest);

        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> responses =
                orderservice.getAllOrderResponses();

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}