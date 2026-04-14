package com.community.groupon.controller;

import com.community.groupon.common.Result;
import com.community.groupon.entity.OrderLogistics;
import com.community.groupon.service.OrderLogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {

    @Autowired
    private OrderLogisticsService orderLogisticsService;

    @GetMapping("/order/{orderId}")
    public Result<List<OrderLogistics>> getByOrderId(@PathVariable Long orderId) {
        return Result.success(orderLogisticsService.findByOrderId(orderId));
    }

    @GetMapping("/order/{orderId}/latest")
    public Result<OrderLogistics> getLatestByOrderId(@PathVariable Long orderId) {
        return Result.success(orderLogisticsService.getLatestByOrderId(orderId));
    }

    @PostMapping
    public Result<OrderLogistics> create(@RequestBody OrderLogistics logistics) {
        logistics.setCreateTime(new java.util.Date());
        logistics.setUpdateTime(new java.util.Date());
        orderLogisticsService.save(logistics);
        return Result.success(logistics);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        OrderLogistics logistics = orderLogisticsService.getById(id);
        if (logistics != null) {
            logistics.setStatus(Integer.valueOf(body.get("status").toString()));
            if (body.get("currentLocation") != null) {
                logistics.setCurrentLocation(body.get("currentLocation").toString());
            }
            logistics.setUpdateTime(new java.util.Date());
            orderLogisticsService.updateById(logistics);
        }
        return Result.success();
    }
}
