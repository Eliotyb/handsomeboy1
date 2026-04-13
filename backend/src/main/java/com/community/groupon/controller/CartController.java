package com.community.groupon.controller;

import com.community.groupon.dto.CartItemDTO;
import com.community.groupon.entity.Cart;
import com.community.groupon.common.Result;
import com.community.groupon.service.CartService;
import com.community.groupon.service.RedisTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisTokenService redisTokenService;

    @GetMapping
    public Result<List<CartItemDTO>> getCart(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (token == null || token.isEmpty()) {
                return Result.success(new ArrayList<>());
            }

            if (!redisTokenService.validateToken(token)) {
                return Result.success(new ArrayList<>());
            }

            Long userId = redisTokenService.getUserId(token);
            if (userId == null) {
                return Result.success(new ArrayList<>());
            }
            List<CartItemDTO> cart = cartService.getCartByUserId(userId);
            return Result.success(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.success(new ArrayList<>());
        }
    }

    @PostMapping
    public Result<Cart> addToCart(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (token == null || token.isEmpty()) {
                return Result.error("请先登录");
            }
            
            if (!redisTokenService.validateToken(token)) {
                return Result.error("Token已过期，请重新登录");
            }

            Long userId = redisTokenService.getUserId(token);
            if (userId == null) {
                return Result.error("用户信息无效，请重新登录");
            }
            
            Long productId = Long.valueOf(requestBody.get("productId").toString());
            Integer quantity = Integer.valueOf(requestBody.get("quantity").toString());
            
            Cart cart = cartService.addToCart(userId, productId, quantity);
            return Result.success(cart);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加购物车失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Void> updateQuantity(@PathVariable Long id, @RequestBody Map<String, Object> requestBody) {
        try {
            Integer quantity = Integer.valueOf(requestBody.get("quantity").toString());
            boolean success = cartService.updateCartQuantity(id, quantity);
            if (success) {
                return Result.success();
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新购物车失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteCartItem(@PathVariable Long id) {
        try {
            boolean success = cartService.deleteCartItem(id);
            if (success) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除购物车项失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public Result<Void> clearCart(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            if (token == null || token.isEmpty()) {
                return Result.error("请先登录");
            }
            
            if (!redisTokenService.validateToken(token)) {
                return Result.error("Token已过期，请重新登录");
            }

            Long userId = redisTokenService.getUserId(token);
            if (userId == null) {
                return Result.error("用户信息无效，请重新登录");
            }
            
            boolean success = cartService.clearCart(userId);
            if (success) {
                return Result.success();
            } else {
                return Result.error("清空购物车失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("清空购物车失败: " + e.getMessage());
        }
    }
}
