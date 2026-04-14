package com.community.groupon.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

@Data
@TableName("order_refund")
public class OrderRefund {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long userId;
    private String refundNo;
    private Integer refundType; // 1:仅退款 2:退货退款
    private String reason;
    private String description;
    private String images;
    private Double refundAmount;
    private Integer status; // 0:待审核 1:待寄回 2:处理中 3:退款成功 4:拒绝/关闭
    private Long auditUserId;
    private String auditRemark;
    private Date auditTime;
    private Date refundTime;
    private String rejectReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
