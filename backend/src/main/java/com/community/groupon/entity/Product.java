package com.community.groupon.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String image;
    private Integer status; // 0: 下架, 1: 上架

    private Date createTime;
    private Date updateTime;
}