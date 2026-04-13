package com.community.groupon.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.community.groupon.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartRepository extends BaseMapper<Cart> {
}
