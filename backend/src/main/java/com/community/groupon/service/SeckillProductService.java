package com.community.groupon.service;

import com.community.groupon.entity.SeckillProduct;
import com.community.groupon.repository.SeckillProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeckillProductService {
    @Autowired
    private SeckillProductRepository seckillProductRepository;

    public List<SeckillProduct> findAll() {
        return seckillProductRepository.selectList(null);
    }

    public SeckillProduct findById(Long id) {
        return seckillProductRepository.selectById(id);
    }

    public SeckillProduct save(SeckillProduct seckillProduct) {
        if (seckillProduct.getId() != null) {
            seckillProductRepository.updateById(seckillProduct);
        } else {
            seckillProductRepository.insert(seckillProduct);
        }
        return seckillProduct;
    }

    public void deleteById(Long id) {
        seckillProductRepository.deleteById(id);
    }
}