package com.community.groupon.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.groupon.entity.OrderReview;
import com.community.groupon.repository.OrderReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderReviewService extends ServiceImpl<OrderReviewRepository, OrderReview> {

    @Autowired
    private OrderReviewRepository orderReviewRepository;

    public List<OrderReview> findByProductId(Long productId) {
        return orderReviewRepository.selectList(
            new QueryWrapper<OrderReview>().eq("product_id", productId).eq("status", 1).orderByDesc("create_time")
        );
    }

    public List<OrderReview> findByUserId(Long userId) {
        return orderReviewRepository.selectList(
            new QueryWrapper<OrderReview>().eq("user_id", userId).orderByDesc("create_time")
        );
    }

    public List<OrderReview> findPendingByUserId(Long userId) {
        return orderReviewRepository.selectList(
            new QueryWrapper<OrderReview>().eq("user_id", userId)
                .isNull("reply_content").eq("status", 1)
        );
    }

    public List<OrderReview> findAll() {
        return orderReviewRepository.selectList(
            new QueryWrapper<OrderReview>().eq("status", 1).orderByDesc("create_time")
        );
    }

    public Double getAvgRating(Long productId) {
        List<OrderReview> reviews = findByProductId(productId);
        if (reviews.isEmpty()) return 5.0;
        double sum = reviews.stream().mapToInt(OrderReview::getRating).sum();
        return Math.round(sum / reviews.size() * 10.0) / 10.0;
    }
}
