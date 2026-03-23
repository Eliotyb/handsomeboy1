package com.community.groupon.service;

import com.community.groupon.entity.SeckillActivity;
import com.community.groupon.repository.SeckillActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillActivityService {
    @Autowired
    private SeckillActivityRepository seckillActivityRepository;

    public List<SeckillActivity> findAll() {
        return seckillActivityRepository.selectList(null);
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
}