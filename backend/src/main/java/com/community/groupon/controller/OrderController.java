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
            String token = extractToken(request);
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
                if (body.get("receiverName") != null) order.setReceiverName(body.get("receiverName").toString());
                if (body.get("receiverPhone") != null) order.setReceiverPhone(body.get("receiverPhone").toString());
                if (body.get("receiverAddress") != null) order.setReceiverAddress(body.get("receiverAddress").toString());
                if (body.get("payMethod") != null) order.setPayMethod(body.get("payMethod").toString());
                order.setPrice(item.getProductPrice().doubleValue());
                order.setQuantity(item.getQuantity());
                double itemTotal = item.getProductPrice().doubleValue() * item.getQuantity();
                order.setTotalPrice(itemTotal);
                order.setStatus(0);
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
            String token = extractToken(request);
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
            if (body.get("receiverName") != null) order.setReceiverName(body.get("receiverName").toString());
            if (body.get("receiverPhone") != null) order.setReceiverPhone(body.get("receiverPhone").toString());
            if (body.get("receiverAddress") != null) order.setReceiverAddress(body.get("receiverAddress").toString());
            if (body.get("payMethod") != null) order.setPayMethod(body.get("payMethod").toString());
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
            String token = extractToken(request);
            if (token == null || !redisTokenService.validateToken(token)) {
                return Result.error("请先登录");
            }
            Long userId = redisTokenService.getUserId(token);
            return Result.success(orderService.findByUserId(userId));
        } catch (Exception e) {
            return Result.error("查询失败");
        }
    }

    @GetMapping("/user/status/{status}")
    public Result<List<Order>> getOrdersByStatus(@PathVariable Integer status, HttpServletRequest request) {
        try {
            String token = extractToken(request);
            if (token == null || !redisTokenService.validateToken(token)) {
                return Result.error("请先登录");
            }
            Long userId = redisTokenService.getUserId(token);
            return Result.success(orderService.findByStatus(status, userId));
        } catch (Exception e) {
            return Result.error("查询失败");
        }
    }

    @PutMapping("/{id}/pay")
    public Result<Void> payOrder(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> body) {
        Order order = orderService.findById(id);
        if (order == null) return Result.error("订单不存在");
        if (order.getStatus() != 0) return Result.error("订单状态不正确");
        order.setStatus(1);
        order.setPayTime(new Date());
        if (body != null && body.get("payMethod") != null) {
            order.setPayMethod(body.get("payMethod").toString());
        }
        order.setUpdateTime(new Date());
        orderService.save(order);
        return Result.success();
    }

    @PutMapping("/{id}/ship")
    public Result<Void> shipOrder(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Order order = orderService.findById(id);
        if (order == null) return Result.error("订单不存在");
        if (order.getStatus() != 1) return Result.error("订单状态不正确");
        order.setStatus(2);
        order.setShipTime(new Date());
        if (body.get("logisticsCompany") != null) order.setLogisticsCompany(body.get("logisticsCompany").toString());
        if (body.get("logisticsNo") != null) order.setLogisticsNo(body.get("logisticsNo").toString());
        order.setUpdateTime(new Date());
        orderService.save(order);
        return Result.success();
    }

    @PutMapping("/{id}/receive")
    public Result<Void> receiveOrder(@PathVariable Long id) {
        Order order = orderService.findById(id);
        if (order == null) return Result.error("订单不存在");
        if (order.getStatus() != 2) return Result.error("订单状态不正确");
        order.setStatus(3);
        order.setReceiveTime(new Date());
        order.setFinishTime(new Date());
        order.setUpdateTime(new Date());
        orderService.save(order);
        return Result.success();
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id, @RequestBody(required = false) Map<String, Object> body) {
        Order order = orderService.findById(id);
        if (order == null) return Result.error("订单不存在");
        if (order.getStatus() > 1) return Result.error("已发货的订单无法取消");
        order.setStatus(4);
        if (body != null && body.get("reason") != null) {
            order.setCancelReason(body.get("reason").toString());
        } else {
            order.setCancelReason("用户取消");
        }
        order.setUpdateTime(new Date());
        orderService.save(order);
        return Result.success();
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

    @GetMapping("/stats")
    public Result<Map<String, Object>> getOrderStats(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            Long userId = redisTokenService.getUserId(token);
            List<Order> orders = orderService.findByUserId(userId);
            int pendingPayment = 0, pendingShipment = 0, pendingReceive = 0, pendingReview = 0, refund = 0;
            for (Order o : orders) {
                switch (o.getStatus()) {
                    case 0: pendingPayment++; break;
                    case 1: pendingShipment++; break;
                    case 2: pendingReceive++; break;
                    case 3: pendingReview++; break;
                    case 5: case 6: refund++; break;
                }
            }
            return Result.success(Map.of(
                "pendingPayment", pendingPayment,
                "pendingShipment", pendingShipment,
                "pendingReceive", pendingReceive,
                "pendingReview", pendingReview,
                "refund", refund
            ));
        } catch (Exception e) {
            return Result.error("获取统计失败");
        }
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }
}
