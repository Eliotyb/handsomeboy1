package com.community.groupon.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.groupon.entity.OrderRefund;
import com.community.groupon.repository.OrderRefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderRefundService extends ServiceImpl<OrderRefundRepository, OrderRefund> {

    @Autowired
    private OrderRefundRepository orderRefundRepository;

    public List<OrderRefund> findByUserId(Long userId) {
        return orderRefundRepository.selectList(
            new QueryWrapper<OrderRefund>().eq("user_id", userId).orderByDesc("create_time")
        );
    }

    public List<OrderRefund> findByStatus(Integer status) {
        return orderRefundRepository.selectList(
            new QueryWrapper<OrderRefund>().eq("status", status).orderByDesc("create_time")
        );
    }

    public List<OrderRefund> findAll() {
        return orderRefundRepository.selectList(null);
    }

    public void audit(Long refundId, Long auditUserId, Integer status, String remark) {
        OrderRefund refund = orderRefundRepository.selectById(refundId);
        if (refund != null) {
            refund.setStatus(status);
            refund.setAuditUserId(auditUserId);
            refund.setAuditRemark(remark);
            refund.setAuditTime(new java.util.Date());
            orderRefundRepository.updateById(refund);
        }
    }

    public void completeRefund(Long refundId) {
        OrderRefund refund = orderRefundRepository.selectById(refundId);
        if (refund != null) {
            refund.setStatus(3);
            refund.setRefundTime(new java.util.Date());
            orderRefundRepository.updateById(refund);
        }
    }
}
