package com.community.groupon.controller;

import com.community.groupon.common.Result;
import com.community.groupon.entity.Order;
import com.community.groupon.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public Result<List<Order>> findAll() {
        return Result.success(orderService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Order> findById(@PathVariable Long id) {
        return Result.success(orderService.findById(id));
    }

    @PostMapping
    public Result<Order> save(@RequestBody Order order) {
        return Result.success(orderService.save(order));
    }

    @PutMapping("/{id}")
    public Result<Order> update(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        return Result.success(orderService.save(order));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        orderService.deleteById(id);
        return Result.success();
    }
}