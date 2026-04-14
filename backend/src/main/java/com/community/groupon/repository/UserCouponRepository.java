package com.community.groupon.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.groupon.entity.UserCoupon;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCouponRepository extends BaseMapper<UserCoupon> {
}
