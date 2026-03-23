package com.community.groupon.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

@Data
@TableName("seckill_activity")
public class SeckillActivity {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private Date startTime;
    private Date endTime;
    private Integer status; // 0: 未开始, 1: 进行中, 2: 已结束

    private Date createTime;
    private Date updateTime;
}