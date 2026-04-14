package com.community.groupon.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.groupon.common.Result;
import com.community.groupon.entity.OrderRefund;
import com.community.groupon.entity.Order;
import com.community.groupon.service.OrderRefundService;
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
@RequestMapping("/api/refunds")
public class RefundController {

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTokenService redisTokenService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @PostMapping("/apply")
    public Result<OrderRefund> apply(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            String token = extractToken(request);
            Long userId = redisTokenService.getUserId(token);

            Long orderId = Long.valueOf(body.get("orderId").toString());
            Integer refundType = body.get("refundType") != null ? Integer.valueOf(body.get("refundType").toString()) : 1;
            String reason = body.get("reason") != null ? body.get("reason").toString() : "";
            String description = body.get("description") != null ? body.get("description").toString() : "";
            Double amount = Double.valueOf(body.get("amount").toString());

            Order order = orderService.findById(orderId);
            if (order == null) return Result.error("订单不存在");

            OrderRefund refund = new OrderRefund();
            refund.setOrderId(orderId);
            refund.setUserId(userId);
            refund.setRefundNo("REF-" + sdf.format(new Date()) + "-" + userId);
            refund.setRefundType(refundType);
            refund.setReason(reason);
            refund.setDescription(description);
            refund.setRefundAmount(amount);
            refund.setStatus(0);
            refund.setCreateTime(new Date());
            refund.setUpdateTime(new Date());
            orderRefundService.save(refund);

            order.setStatus(5);
            order.setUpdateTime(new Date());
            orderService.save(order);

            return Result.success(refund);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("申请失败: " + e.getMessage());
        }
    }

    @GetMapping("/my")
    public Result<List<OrderRefund>> getMyRefunds(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            Long userId = redisTokenService.getUserId(token);
            return Result.success(orderRefundService.findByUserId(userId));
        } catch (Exception e) {
            return Result.error("请先登录");
        }
    }

    @GetMapping("/list")
    public Result<List<OrderRefund>> listAll() {
        return Result.success(orderRefundService.findAll());
    }

    @GetMapping("/list/status/{status}")
    public Result<List<OrderRefund>> listByStatus(@PathVariable Integer status) {
        return Result.success(orderRefundService.findByStatus(status));
    }

    @PutMapping("/{id}/audit")
    public Result<Void> audit(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long auditUserId = Long.valueOf(body.get("auditUserId").toString());
        Integer status = Integer.valueOf(body.get("status").toString());
        String remark = body.get("remark") != null ? body.get("remark").toString() : "";

        if (status == 4 && body.get("rejectReason") != null) {
            OrderRefund refund = orderRefundService.getById(id);
            if (refund != null) {
                refund.setRejectReason(body.get("rejectReason").toString());
                orderRefundService.updateById(refund);
            }
        }

        orderRefundService.audit(id, auditUserId, status, remark);

        if (status == 3) {
            orderRefundService.completeRefund(id);
        }
        return Result.success();
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return token;
    }
}
