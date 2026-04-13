package com.community.groupon.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private String productImage;
    private Integer productStock;
    private Boolean checked = true;
}
