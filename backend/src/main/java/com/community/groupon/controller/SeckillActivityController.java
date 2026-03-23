package com.community.groupon.controller;

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
    public List<SeckillActivity> findAll() {
        return seckillActivityService.findAll();
    }

    @GetMapping("/{id}")
    public SeckillActivity findById(@PathVariable Long id) {
        return seckillActivityService.findById(id);
    }

    @PostMapping
    public SeckillActivity save(@RequestBody SeckillActivity seckillActivity) {
        return seckillActivityService.save(seckillActivity);
    }

    @PutMapping("/{id}")
    public SeckillActivity update(@PathVariable Long id, @RequestBody SeckillActivity seckillActivity) {
        seckillActivity.setId(id);
        return seckillActivityService.save(seckillActivity);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        seckillActivityService.deleteById(id);
    }
}