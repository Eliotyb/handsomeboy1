package com.community.groupon.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.groupon.entity.OrderLogistics;
import com.community.groupon.repository.OrderLogisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderLogisticsService extends ServiceImpl<OrderLogisticsRepository, OrderLogistics> {

    @Autowired
    private OrderLogisticsRepository orderLogisticsRepository;

    public List<OrderLogistics> findByOrderId(Long orderId) {
        return orderLogisticsRepository.selectList(
            new QueryWrapper<OrderLogistics>().eq("order_id", orderId).orderByDesc("create_time")
        );
    }

    public OrderLogistics getLatestByOrderId(Long orderId) {
        List<OrderLogistics> list = findByOrderId(orderId);
        return list.isEmpty() ? null : list.get(0);
    }
}
