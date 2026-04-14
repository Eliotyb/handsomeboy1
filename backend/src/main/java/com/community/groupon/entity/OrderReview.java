package com.community.groupon.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

@Data
@TableName("order_review")
public class OrderReview {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long productId;
    private Long userId;
    private Integer rating; // 1-5星
    private String content;
    private String images;
    private Integer isAnonymous; // 0:否 1:是
    private String replyContent;
    private Date replyTime;
    private Integer status; // 0:隐藏 1:显示

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
