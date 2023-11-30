package com.example.springboot_redis.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtil {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String data) {

    }

}
