package com.community.groupon.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

@Data
@TableName("`order`")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;
    private Long userId;
    private Long productId;
    private Long seckillProductId;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
    private Integer status; // 0: 待支付, 1: 已支付, 2: 已发货, 3: 已完成, 4: 已取消

    private Date createTime;
    private Date updateTime;
}