package com.community.groupon.repository;

import com.community.groupon.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends BaseMapper<Order> {
    
    @Select("SELECT COALESCE(SUM(total_price), 0) FROM `order` WHERE status IN (1, 2, 3)")
    Double calculateTotalAmount();
}