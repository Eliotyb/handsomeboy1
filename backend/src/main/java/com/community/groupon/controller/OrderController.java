package com.community.groupon.controller;

import com.community.groupon.common.Result;
import com.community.groupon.dto.CartItemDTO;
import com.community.groupon.entity.Order;
import com.community.groupon.service.CartService;
import com.community.groupon.service.OrderService;
import com.community.groupon.service.RedisTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisTokenService redisTokenService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    @PostMapping("/create")
    public Result<Map<String, Object>> createOrder(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (token == null || !redisTokenService.validateToken(token)) {
                return Result.error("请先登录");
            }
            Long userId = redisTokenService.getUserId(token);

            List<CartItemDTO> cartItems = cartService.getCartByUserId(userId);
            if (cartItems == null || cartItems.isEmpty()) {
                return Result.error("购物车为空");
            }

            String orderNo = "ORD-" + sdf.format(new Date()) + "-" + userId;
            double totalAmount = 0;
            int totalCount = 0;

            for (CartItemDTO item : cartItems) {
                Order order = new Order();
                order.setOrderNo(orderNo);
                order.setUserId(userId);
                order.setProductId(item.getProductId());
                order.setSeckillProductId(null);
                order.setPrice(item.getProductPrice().doubleValue());
                order.setQuantity(item.getQuantity());
                double itemTotal = item.getProductPrice().doubleValue() * item.getQuantity();
                order.setTotalPrice(itemTotal);
                order.setStatus(1);
                order.setCreateTime(new Date());
                order.setUpdateTime(new Date());
                orderService.save(order);
                totalAmount += itemTotal;
                totalCount += item.getQuantity();
            }

            cartService.clearCart(userId);

            Map<String, Object> resultData = Map.of(
                "orderNo", orderNo,
                "totalAmount", Math.round(totalAmount * 100.0) / 100.0,
                "totalCount", totalCount
            );
            return Result.success(resultData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建订单失败: " + e.getMessage());
        }
    }

    @PostMapping("/seckill")
    public Result<Map<String, Object>> createSeckillOrder(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (token == null || !redisTokenService.validateToken(token)) {
                return Result.error("请先登录");
            }
            Long userId = redisTokenService.getUserId(token);

            Long productId = Long.valueOf(body.get("productId").toString());
            Double price = Double.valueOf(body.get("price").toString());
            Integer quantity = body.get("quantity") != null ? Integer.valueOf(body.get("quantity").toString()) : 1;

            String orderNo = "SEC-" + sdf.format(new Date()) + "-" + userId;
            double totalAmount = price * quantity;

            Order order = new Order();
            order.setOrderNo(orderNo);
            order.setUserId(userId);
            order.setProductId(productId);
            order.setSeckillProductId(null);
            order.setPrice(price);
            order.setQuantity(quantity);
            order.setTotalPrice(totalAmount);
            order.setStatus(0);
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            orderService.save(order);

            Map<String, Object> resultData = Map.of(
                "orderNo", orderNo,
                "totalAmount", Math.round(totalAmount * 100.0) / 100.0
            );
            return Result.success(resultData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("秒杀订单创建失败: " + e.getMessage());
        }
    }

    @GetMapping
    public Result<List<Order>> findAll() {
        return Result.success(orderService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Order> findById(@PathVariable Long id) {
        return Result.success(orderService.findById(id));
    }

    @GetMapping("/user/my-orders")
    public Result<List<Order>> getMyOrders(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (token == null || !redisTokenService.validateToken(token)) {
                return Result.error("请先登录");
            }
            Long userId = redisTokenService.getUserId(token);
            return Result.success(orderService.findByUserId(userId));
        } catch (Exception e) {
            return Result.error("查询失败");
        }
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
