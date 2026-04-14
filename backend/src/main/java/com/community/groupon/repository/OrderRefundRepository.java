package com.community.groupon.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.groupon.entity.OrderRefund;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderRefundRepository extends BaseMapper<OrderRefund> {
}
