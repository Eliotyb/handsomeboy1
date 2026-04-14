package com.community.groupon.controller;

import com.community.groupon.common.Result;
import com.community.groupon.entity.Coupon;
import com.community.groupon.service.CouponService;
import com.community.groupon.service.RedisTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private RedisTokenService redisTokenService;

    /**
     * 获取用户所有优惠券
     */
    @GetMapping("/user")
    public Result<List<Coupon>> getUserCoupons(HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            List<Coupon> coupons = couponService.getUserCoupons(userId);
            return Result.success(coupons);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取可领取的优惠券列表
     */
    @GetMapping("/available")
    public Result<List<Coupon>> getAvailableCoupons() {
        try {
            List<Coupon> coupons = couponService.getAvailableCoupons();
            return Result.success(coupons);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 领取优惠券
     */
    @PostMapping("/{couponId}/receive")
    public Result<Map<String, Object>> receiveCoupon(
            @PathVariable Long couponId,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            boolean success = couponService.receiveCoupon(userId, couponId);
            
            if (success) {
                return Result.success(Map.of("message", "领取成功"));
            } else {
                return Result.error("领取失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 使用优惠券
     */
    @PostMapping("/{couponId}/use")
    public Result<Map<String, Object>> useCoupon(
            @PathVariable Long couponId,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        try {
            Long userId = getUserIdFromToken(request);
            Long orderId = Long.valueOf(body.get("orderId").toString());
            boolean success = couponService.useCoupon(userId, couponId, orderId);
            
            if (success) {
                return Result.success(Map.of("message", "使用成功"));
            } else {
                return Result.error("使用失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    private Long getUserIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (redisTokenService.validateToken(token)) {
                return redisTokenService.getUserId(token);
            }
        }
        throw new RuntimeException("请先登录");
    }
}
