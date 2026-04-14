package com.community.groupon.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

@Data
@TableName("order_logistics")
public class OrderLogistics {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private String logisticsCompany;
    private String logisticsNo;
    private Integer status; // 0:待揽收 1:已揽收 2:运输中 3:派送中 4:已签收 5:异常
    private String currentLocation;
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
