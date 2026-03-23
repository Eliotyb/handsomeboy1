package com.community.groupon.controller;

import com.community.groupon.entity.SeckillProduct;
import com.community.groupon.service.SeckillProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seckill-products")
public class SeckillProductController {
    @Autowired
    private SeckillProductService seckillProductService;

    @GetMapping
    public List<SeckillProduct> findAll() {
        return seckillProductService.findAll();
    }

    @GetMapping("/{id}")
    public SeckillProduct findById(@PathVariable Long id) {
        return seckillProductService.findById(id);
    }

    @PostMapping
    public SeckillProduct save(@RequestBody SeckillProduct seckillProduct) {
        return seckillProductService.save(seckillProduct);
    }

    @PutMapping("/{id}")
    public SeckillProduct update(@PathVariable Long id, @RequestBody SeckillProduct seckillProduct) {
        seckillProduct.setId(id);
        return seckillProductService.save(seckillProduct);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        seckillProductService.deleteById(id);
    }
}