package com.community.groupon.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.groupon.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponRepository extends BaseMapper<Coupon> {
}
