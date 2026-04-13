package com.community.groupon.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOKEN_PREFIX = "token:";
    private static final long TOKEN_EXPIRE_HOURS = 24;

    private final Map<String, Long> tokenExpiryMap = new ConcurrentHashMap<>();
    private final Map<String, String> memoryTokenStore = new ConcurrentHashMap<>();

    public void saveToken(String token, Long userId, String username, Integer role) {
        try {
            String key = TOKEN_PREFIX + token;
            String value = userId + ":" + username + ":" + role;

            if (redisTemplate != null) {
                try {
                    redisTemplate.opsForValue().set(key, value, TOKEN_EXPIRE_HOURS, TimeUnit.HOURS);
                    return;
                } catch (Exception e) {
                    System.out.println("[Warning] Redis不可用，切换到内存存储");
                }
            }

            memoryTokenStore.put(key, value);
            tokenExpiryMap.put(key, System.currentTimeMillis() + TOKEN_EXPIRE_HOURS * 60 * 60 * 1000);
            System.out.println("[Info] Token已保存到内存: " + key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateToken(String token) {
        try {
            String key = TOKEN_PREFIX + token;

            if (redisTemplate != null) {
                try {
                    Boolean hasKey = redisTemplate.hasKey(key);
                    return hasKey != null && hasKey;
                } catch (Exception e) {
                    System.out.println("[Warning] Redis验证失败，尝试内存验证");
                }
            }

            if (memoryTokenStore.containsKey(key)) {
                Long expiryTime = tokenExpiryMap.get(key);
                if (expiryTime != null && System.currentTimeMillis() < expiryTime) {
                    return true;
                } else {
                    memoryTokenStore.remove(key);
                    tokenExpiryMap.remove(key);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        try {
            String key = TOKEN_PREFIX + token;
            String value = null;

            if (redisTemplate != null) {
                try {
                    value = redisTemplate.opsForValue().get(key);
                } catch (Exception e) {
                    System.out.println("[Warning] Redis获取失败，尝试从内存获取");
                }
            }

            if (value == null) {
                value = memoryTokenStore.get(key);
            }

            if (value != null) {
                String[] parts = value.split(":");
                return Long.parseLong(parts[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteToken(String token) {
        try {
            String key = TOKEN_PREFIX + token;

            if (redisTemplate != null) {
                try {
                    redisTemplate.delete(key);
                    return;
                } catch (Exception e) {
                    System.out.println("[Warning] Redis删除失败，尝试从内存删除");
                }
            }

            memoryTokenStore.remove(key);
            tokenExpiryMap.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
