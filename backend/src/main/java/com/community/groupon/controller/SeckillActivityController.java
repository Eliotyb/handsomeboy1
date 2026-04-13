package com.community.groupon.controller;

import com.community.groupon.common.Result;
import com.community.groupon.dto.ActivityWithStockDTO;
import com.community.groupon.entity.SeckillActivity;
import com.community.groupon.service.SeckillActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seckill-activities")
public class SeckillActivityController {
    @Autowired
    private SeckillActivityService seckillActivityService;

    @GetMapping
    public Result<List<ActivityWithStockDTO>> findAll() {
        return Result.success(seckillActivityService.findAllWithStock());
    }

    @GetMapping("/{id}")
    public Result<SeckillActivity> findById(@PathVariable Long id) {
        return Result.success(seckillActivityService.findById(id));
    }

    @PostMapping
    public Result<SeckillActivity> save(@RequestBody SeckillActivity seckillActivity) {
        return Result.success(seckillActivityService.save(seckillActivity));
    }

    @PutMapping("/{id}")
    public Result<SeckillActivity> update(@PathVariable Long id, @RequestBody SeckillActivity seckillActivity) {
        seckillActivity.setId(id);
        return Result.success(seckillActivityService.save(seckillActivity));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        seckillActivityService.deleteById(id);
        return Result.success();
    }
}