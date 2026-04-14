package com.community.groupon.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("coupon")
public class Coupon {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;
    private String description;
    
    /**
     * 优惠券类型：0-满减券，1-折扣券，2-无门槛券
     */
    private Integer couponType;
    
    /**
     * 优惠值（金额或折扣比例）
     */
    private BigDecimal value;
    
    /**
     * 最低消费金额
     */
    private BigDecimal minAmount;
    
    /**
     * 最大优惠金额
     */
    private BigDecimal maxDiscount;
    
    /**
     * 发放总量
     */
    private Integer totalCount;
    
    /**
     * 已使用数量
     */
    private Integer usedCount;
    
    /**
     * 状态：0-停用，1-启用
     */
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    
    /**
     * 用户ID（NULL表示通用券）
     */
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
