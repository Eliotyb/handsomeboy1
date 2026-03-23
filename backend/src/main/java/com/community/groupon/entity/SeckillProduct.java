package com.community.groupon.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

@Data
@TableName("seckill_product")
public class SeckillProduct {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long activityId;
    private Long productId;
    private Double seckillPrice;
    private Integer seckillStock;
    private Integer soldCount;

    private Date createTime;
    private Date updateTime;
}