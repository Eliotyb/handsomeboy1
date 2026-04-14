package com.community.groupon.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.groupon.entity.OrderLogistics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderLogisticsRepository extends BaseMapper<OrderLogistics> {
}
