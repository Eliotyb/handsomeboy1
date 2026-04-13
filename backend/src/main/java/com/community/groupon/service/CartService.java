package com.community.groupon.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.community.groupon.dto.CartItemDTO;
import com.community.groupon.entity.Cart;
import com.community.groupon.entity.Product;
import com.community.groupon.repository.CartRepository;
import com.community.groupon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItemDTO> getCartByUserId(Long userId) {
        try {
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUserId, userId);
            wrapper.orderByDesc(Cart::getCreateTime);
            List<Cart> cartList = cartRepository.selectList(wrapper);
            
            if (cartList.isEmpty()) {
                return new ArrayList<>();
            }
            
            List<CartItemDTO> result = new ArrayList<>();
            for (Cart cart : cartList) {
                CartItemDTO dto = new CartItemDTO();
                dto.setId(cart.getId());
                dto.setUserId(cart.getUserId());
                dto.setProductId(cart.getProductId());
                dto.setQuantity(cart.getQuantity());
                
                Product product = productRepository.selectById(cart.getProductId());
                if (product != null) {
                    dto.setProductName(product.getName());
                    dto.setProductDescription(product.getDescription());
                    dto.setProductPrice(BigDecimal.valueOf(product.getPrice()));
                    dto.setProductImage(product.getImage());
                    dto.setProductStock(product.getStock());
                } else {
                    dto.setProductName("商品" + cart.getProductId());
                    dto.setProductDescription("商品已下架");
                    dto.setProductPrice(BigDecimal.valueOf(0));
                    dto.setProductImage("");
                    dto.setProductStock(0);
                }
                result.add(dto);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        try {
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUserId, userId);
            wrapper.eq(Cart::getProductId, productId);
            Cart existingCart = cartRepository.selectOne(wrapper);
            
            if (existingCart != null) {
                existingCart.setQuantity(existingCart.getQuantity() + quantity);
                existingCart.setUpdateTime(new Date());
                cartRepository.updateById(existingCart);
                return existingCart;
            } else {
                Cart cart = new Cart();
                cart.setUserId(userId);
                cart.setProductId(productId);
                cart.setQuantity(quantity);
                cart.setCreateTime(new Date());
                cart.setUpdateTime(new Date());
                cartRepository.insert(cart);
                return cart;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateCartQuantity(Long id, Integer quantity) {
        try {
            Cart cart = cartRepository.selectById(id);
            if (cart == null) {
                return false;
            }
            cart.setQuantity(quantity);
            cart.setUpdateTime(new Date());
            return cartRepository.updateById(cart) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCartItem(Long id) {
        try {
            return cartRepository.deleteById(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearCart(Long userId) {
        try {
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUserId, userId);
            return cartRepository.delete(wrapper) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
