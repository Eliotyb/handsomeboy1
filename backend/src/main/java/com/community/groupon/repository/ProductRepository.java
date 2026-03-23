package com.community.groupon.repository;

import com.community.groupon.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseMapper<Product> {
}