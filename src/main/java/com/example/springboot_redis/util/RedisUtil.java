package com.example.springboot_redis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtil {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    /**
     *
     * @param key
     * @param data
     */
    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    /**
     * 문자열 key, value 저장 및 지속시간 설정
     * @param key
     * @param data
     * @param duration
     */
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    /**
     *
     * @param key
     * @return
     */
    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }

    /**
     * 저장된 여러 key 중 특정 문자열이 들어간 key list를 불러옴
     * @param keys
     * @return
     */
    public List<String> getMultiKeys(String keys) {
        ScanOptions so = ScanOptions.scanOptions().match("*"+keys+"*").build();
        Cursor<byte[]> cs = redisTemplate.getConnectionFactory().getConnection().scan(so);

        List<String> results = new ArrayList<>();
        if (cs.hasNext()) {
            while (cs.hasNext()) {
                String key = new String(cs.next());
                results.add(key);
            }
            return results;
        }

        return null;
    }

    /**
     *
     * @param key
     */
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 입력한 키에 대해 지속시간을 설정
     * @param key
     * @param timeout
     */
    public void expireValues(String key, int timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 저장된 key, hashKey 값을 입력하여 value 값을 호출
     * @param key
     * @param hashKey
     * @return
     */
    public String getHashValue(String key, Object hashKey) {
        HashOperations<String, Object, Object> hashValue = redisTemplate.opsForHash();

        if (hashValue.get(key, hashKey) == null) {
            return "false";
        }

        return (String) hashValue.get(key, hashKey);
    }

    /**
     *
     * @param key
     * @param hashKey
     * @param value
     */
    public void setHashValue(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hashValue = redisTemplate.opsForHash();
        hashValue.put(key, hashKey, value);
    }

    /**
     *
     * @param key
     * @param hashKey
     */
    public void deleteHash(String key, Object hashKey) {
        HashOperations<String, Object, Object> hashValue = redisTemplate.opsForHash();
        hashValue.delete(key, hashKey);
    }

}
