package com.community.groupon.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.community.groupon.dto.ActivityWithStockDTO;
import com.community.groupon.entity.SeckillActivity;
import com.community.groupon.entity.SeckillProduct;
import com.community.groupon.repository.SeckillActivityRepository;
import com.community.groupon.repository.SeckillProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeckillActivityService {
    @Autowired
    private SeckillActivityRepository seckillActivityRepository;
    
    @Autowired
    private SeckillProductRepository seckillProductRepository;

    public List<SeckillActivity> findAll() {
        return seckillActivityRepository.selectList(null);
    }
    
    public List<ActivityWithStockDTO> findAllWithStock() {
        List<SeckillActivity> activities = seckillActivityRepository.selectList(null);
        return activities.stream().map(activity -> {
            QueryWrapper<SeckillProduct> wrapper = new QueryWrapper<>();
            wrapper.eq("activity_id", activity.getId());
            wrapper.last("LIMIT 1");
            SeckillProduct product = seckillProductRepository.selectOne(wrapper);
            return ActivityWithStockDTO.fromEntity(
                activity,
                product != null ? product.getSeckillStock() : 0,
                product != null ? product.getSoldCount() : 0
            );
        }).collect(Collectors.toList());
    }

    public SeckillActivity findById(Long id) {
        return seckillActivityRepository.selectById(id);
    }

    public SeckillActivity save(SeckillActivity seckillActivity) {
        if (seckillActivity.getId() != null) {
            seckillActivityRepository.updateById(seckillActivity);
        } else {
            seckillActivityRepository.insert(seckillActivity);
        }
        return seckillActivity;
    }

    public void deleteById(Long id) {
        seckillActivityRepository.deleteById(id);
    }

    public long count() {
        return seckillActivityRepository.selectCount(null);
    }
}