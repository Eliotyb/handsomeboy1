package com.community.groupon.service;

import com.community.groupon.entity.Order;
import com.community.groupon.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.selectList(null);
    }

    public Order findById(Long id) {
        return orderRepository.selectById(id);
    }

    public Order save(Order order) {
        if (order.getId() != null) {
            orderRepository.updateById(order);
        } else {
            orderRepository.insert(order);
        }
        return order;
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}