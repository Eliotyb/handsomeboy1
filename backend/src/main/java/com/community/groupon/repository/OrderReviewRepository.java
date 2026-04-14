package com.community.groupon.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.groupon.entity.OrderReview;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderReviewRepository extends BaseMapper<OrderReview> {
}
