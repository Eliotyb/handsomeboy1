package com.community.groupon.controller;

import com.community.groupon.common.Result;
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
    public Result<List<SeckillProduct>> findAll() {
        return Result.success(seckillProductService.findAll());
    }

    @GetMapping("/{id}")
    public Result<SeckillProduct> findById(@PathVariable Long id) {
        return Result.success(seckillProductService.findById(id));
    }

    @PostMapping
    public Result<SeckillProduct> save(@RequestBody SeckillProduct seckillProduct) {
        return Result.success(seckillProductService.save(seckillProduct));
    }

    @PutMapping("/{id}")
    public Result<SeckillProduct> update(@PathVariable Long id, @RequestBody SeckillProduct seckillProduct) {
        seckillProduct.setId(id);
        return Result.success(seckillProductService.save(seckillProduct));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        seckillProductService.deleteById(id);
        return Result.success();
    }
}