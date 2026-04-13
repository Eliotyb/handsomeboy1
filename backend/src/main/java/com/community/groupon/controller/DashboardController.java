package com.community.groupon.controller;

import com.community.groupon.common.Result;
import com.community.groupon.service.OrderService;
import com.community.groupon.service.ProductService;
import com.community.groupon.service.SeckillActivityService;
import com.community.groupon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long userCount = userService.count();
        long productCount = productService.count();
        long seckillActivityCount = seckillActivityService.count();
        long orderCount = orderService.count();
        Double totalAmount = orderService.getTotalAmount();
        
        stats.put("userCount", userCount);
        stats.put("productCount", productCount);
        stats.put("seckillActivityCount", seckillActivityCount);
        stats.put("orderCount", orderCount);
        stats.put("totalAmount", totalAmount != null ? String.format("%.0f", totalAmount) : "0");
        
        return Result.success(stats);
    }
}
