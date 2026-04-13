package com.community.groupon.dto;

import com.community.groupon.entity.SeckillActivity;
import lombok.Data;

@Data
public class ActivityWithStockDTO {
    private Long id;
    private String name;
    private Integer status;
    private Integer seckillStock;
    private Integer soldCount;
    
    public static ActivityWithStockDTO fromEntity(SeckillActivity activity, Integer stock, Integer sold) {
        ActivityWithStockDTO dto = new ActivityWithStockDTO();
        dto.setId(activity.getId());
        dto.setName(activity.getName());
        dto.setStatus(activity.getStatus());
        dto.setSeckillStock(stock != null ? stock : 0);
        dto.setSoldCount(sold != null ? sold : 0);
        return dto;
    }
}
