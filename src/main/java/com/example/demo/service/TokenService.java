package com.example.demo.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // Token 有效期：2 小时
    private static final long EXPIRE_HOURS = 2;

    // 生成 Token，存入 Redis
    public String generateToken(int userId, String username) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String key = "token:" + token;
        String value = userId + ":" + username;
        redisTemplate.opsForValue().set(key, value, EXPIRE_HOURS, TimeUnit.HOURS);
        return token;
    }

    // 根据 Token 取用户信息，取不到返回 null
    public String getUsernameByToken(String token) {
        String value = redisTemplate.opsForValue().get("token:" + token);
        if (value == null) return null;
        // 续期：每次访问刷新过期时间
        redisTemplate.expire("token:" + token, EXPIRE_HOURS, TimeUnit.HOURS);
        return value.split(":")[1];
    }

    // 退出登录：删除 Token
    public void deleteToken(String token) {
        redisTemplate.delete("token:" + token);
    }
}