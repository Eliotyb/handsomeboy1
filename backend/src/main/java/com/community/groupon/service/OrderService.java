package com.community.groupon.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    public long count() {
        return orderRepository.selectCount(null);
    }
    
    public Double getTotalAmount() {
        return orderRepository.calculateTotalAmount();
    }

    public List<Order> findByUserId(Long userId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        wrapper.orderByDesc(Order::getCreateTime);
        return orderRepository.selectList(wrapper);
    }

    public List<Order> findByStatus(Integer status, Long userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        if (userId != null) wrapper.eq("user_id", userId);
        if (status != null) wrapper.eq("status", status);
        wrapper.orderByDesc("create_time");
        return orderRepository.selectList(wrapper);
    }
}