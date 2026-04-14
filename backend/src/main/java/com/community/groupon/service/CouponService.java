package com.community.groupon.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.groupon.entity.Coupon;
import com.community.groupon.entity.UserCoupon;
import com.community.groupon.repository.CouponRepository;
import com.community.groupon.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    /**
     * 获取用户所有优惠券（包含状态信息）
     */
    public List<Coupon> getUserCoupons(Long userId) {
        QueryWrapper<UserCoupon> userCouponQuery = new QueryWrapper<>();
        userCouponQuery.eq("user_id", userId);
        List<UserCoupon> userCoupons = userCouponRepository.selectList(userCouponQuery);
        
        List<Long> couponIds = userCoupons.stream()
                .map(UserCoupon::getCouponId)
                .collect(Collectors.toList());
        
        if (couponIds.isEmpty()) {
            return List.of();
        }
        
        QueryWrapper<Coupon> couponQuery = new QueryWrapper<>();
        couponQuery.in("id", couponIds);
        List<Coupon> coupons = couponRepository.selectList(couponQuery);
        
        coupons.forEach(coupon -> {
            UserCoupon userCoupon = userCoupons.stream()
                    .filter(uc -> uc.getCouponId().equals(coupon.getId()))
                    .findFirst()
                    .orElse(null);
            if (userCoupon != null) {
                coupon.setStatus(userCoupon.getStatus());
            }
        });
        
        return coupons;
    }

    /**
     * 获取可领取的优惠券列表（通用券）
     */
    public List<Coupon> getAvailableCoupons() {
        Date now = new Date();
        QueryWrapper<Coupon> query = new QueryWrapper<>();
        query.eq("status", 1)
             .eq("user_id", null)
             .le("start_time", now)
             .ge("end_time", now)
             .gt("total_count", 0)
             .orderByDesc("create_time");
        return couponRepository.selectList(query);
    }

    /**
     * 领取优惠券
     */
    public boolean receiveCoupon(Long userId, Long couponId) {
        Coupon coupon = couponRepository.selectById(couponId);
        if (coupon == null) {
            throw new RuntimeException("优惠券不存在");
        }
        if (coupon.getStatus() != 1) {
            throw new RuntimeException("优惠券已停用");
        }
        Date now = new Date();
        if (now.before(coupon.getStartTime()) || now.after(coupon.getEndTime())) {
            throw new RuntimeException("优惠券不在有效期内");
        }
        if (coupon.getTotalCount() <= coupon.getUsedCount()) {
            throw new RuntimeException("优惠券已领完");
        }

        QueryWrapper<UserCoupon> existQuery = new QueryWrapper<>();
        existQuery.eq("user_id", userId).eq("coupon_id", couponId);
        Long count = userCouponRepository.selectCount(existQuery);
        if (count > 0) {
            throw new RuntimeException("已领取过该优惠券");
        }

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0);
        userCoupon.setReceiveTime(now);
        userCouponRepository.insert(userCoupon);

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.updateById(coupon);

        return true;
    }

    /**
     * 使用优惠券
     */
    public boolean useCoupon(Long userId, Long couponId, Long orderId) {
        QueryWrapper<UserCoupon> query = new QueryWrapper<>();
        query.eq("user_id", userId)
             .eq("coupon_id", couponId)
             .eq("status", 0);
        UserCoupon userCoupon = userCouponRepository.selectOne(query);
        
        if (userCoupon == null) {
            throw new RuntimeException("未找到可用优惠券");
        }

        userCoupon.setStatus(1);
        userCoupon.setOrderId(orderId);
        userCoupon.setUseTime(new Date());
        userCouponRepository.updateById(userCoupon);

        Coupon coupon = couponRepository.selectById(couponId);
        if (coupon != null) {
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            couponRepository.updateById(coupon);
        }

        return true;
    }
}
