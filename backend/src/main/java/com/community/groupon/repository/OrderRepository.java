package com.community.groupon.repository;

import com.community.groupon.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseMapper<Order> {
}