package com.livein.springecom.repo;

import com.livein.springecom.model.Order;
import com.livein.springecom.model.dto.OrderResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Integer> {
    Optional<Order> findByOrderId(String orderId);


}
