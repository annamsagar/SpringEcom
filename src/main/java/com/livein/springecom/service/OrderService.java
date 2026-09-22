package com.livein.springecom.service;

import com.livein.springecom.model.Order;
import com.livein.springecom.model.OrderItem;
import com.livein.springecom.model.Product;
import com.livein.springecom.model.dto.OrderItemRequest;
import com.livein.springecom.model.dto.OrderItemResponse;
import com.livein.springecom.model.dto.OrderRequest;
import com.livein.springecom.model.dto.OrderResponse;
import com.livein.springecom.repo.OrderRepo;
import com.livein.springecom.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private ProductRepo productRepo;
    public OrderResponse placeOrder(OrderRequest request) {

        Order order=new Order();
        String orderId="ORD"+UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setStatus("Placed");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems=new ArrayList<>();
        for(OrderItemRequest itemreq:request.items()){
            Product product=productRepo.findById(itemreq.productId())
                    .orElseThrow(()->new RuntimeException("product not found"));

            product.setStockQuantity(product.getStockQuantity()- itemreq.quantity());
            productRepo.save(product);

            OrderItem orderItem=OrderItem.builder()
                    .product(product)
                    .quantity(itemreq.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemreq.quantity())))
                    .order(order)
                    .build();
            orderItems.add(orderItem);

        }

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);


        List<OrderItemResponse> itemResponses=new ArrayList<>();
        for(OrderItem item:order.getOrderItems()){
            OrderItemResponse orderItemResponse=new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );
            itemResponses.add(orderItemResponse);
        }
        OrderResponse orderResponse=new OrderResponse(savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                itemResponses
                );


        return orderResponse;
    }

    public List<OrderResponse> getAllOrderResponses() {
        List<Order> orders=orderRepo.findAll();
        List<OrderResponse> orderResponses=new ArrayList<>();

        for(Order order:orders){


            List<OrderItemResponse> itemResponses=new ArrayList<>();

            for(OrderItem item:order.getOrderItems()){
                OrderItemResponse orderItemResponse=new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                );
                itemResponses.add(orderItemResponse);
            }
            OrderResponse orderResponse=new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponses
            );
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
}
