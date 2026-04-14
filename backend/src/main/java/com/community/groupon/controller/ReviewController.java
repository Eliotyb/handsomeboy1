package com.community.groupon.controller;

import com.community.groupon.common.Result;
import com.community.groupon.entity.OrderReview;
import com.community.groupon.service.OrderReviewService;
import com.community.groupon.service.RedisTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private OrderReviewService orderReviewService;

    @Autowired
    private RedisTokenService redisTokenService;

    @PostMapping
    public Result<OrderReview> create(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        try {
            String token = extractToken(request);
            Long userId = redisTokenService.getUserId(token);

            OrderReview review = new OrderReview();
            review.setOrderId(Long.valueOf(body.get("orderId").toString()));
            review.setProductId(Long.valueOf(body.get("productId").toString()));
            review.setUserId(userId);
            review.setRating(Integer.valueOf(body.get("rating").toString()));
            review.setContent(body.get("content") != null ? body.get("content").toString() : "");
            review.setIsAnonymous(body.get("isAnonymous") != null ? Integer.valueOf(body.get("isAnonymous").toString()) : 0);
            review.setStatus(1);
            review.setCreateTime(new java.util.Date());
            review.setUpdateTime(new java.util.Date());
            orderReviewService.save(review);
            return Result.success(review);
        } catch (Exception e) {
            return Result.error("评价提交失败: " + e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public Result<List<OrderReview>> getByProduct(@PathVariable Long productId) {
        return Result.success(orderReviewService.findByProductId(productId));
    }

    @GetMapping("/product/{productId}/avg-rating")
    public Result<Map<String, Object>> getAvgRating(@PathVariable Long productId) {
        double avg = orderReviewService.getAvgRating(productId);
        return Result.success(Map.of("avgRating", avg));
    }

    @GetMapping("/my")
    public Result<List<OrderReview>> getMyReviews(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            Long userId = redisTokenService.getUserId(token);
            return Result.success(orderReviewService.findByUserId(userId));
        } catch (Exception e) {
            return Result.error("请先登录");
        }
    }

    @GetMapping("/pending/my")
    public Result<List<OrderReview>> getPendingMyReviews(HttpServletRequest request) {
        try {
            String token = extractToken(request);
            Long userId = redisTokenService.getUserId(token);
            return Result.success(orderReviewService.findPendingByUserId(userId));
        } catch (Exception e) {
            return Result.error("请先登录");
        }
    }

    @GetMapping("/list")
    public Result<List<OrderReview>> listAll() {
        return Result.success(orderReviewService.findAll());
    }

    @PutMapping("/{id}/reply")
    public Result<Void> reply(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        OrderReview review = orderReviewService.getById(id);
        if (review != null) {
            review.setReplyContent(body.get("replyContent").toString());
            review.setReplyTime(new java.util.Date());
            orderReviewService.updateById(review);
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
