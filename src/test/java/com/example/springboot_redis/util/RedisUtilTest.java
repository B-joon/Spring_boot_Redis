package com.example.springboot_redis.util;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisUtilTest {

    final String KEY = "key";
    final String VALUE = "value";
    final Duration DURATION = Duration.ofMillis(5000);

    @Autowired
    RedisUtil redisUtil;

    private final static Logger log = LoggerFactory.getLogger(RedisUtilTest.class);

    @DisplayName("Redis 데이터 저장 테스트")
    @Test
    void redis_data_save_test() {
        // given
        String key = "title";
        String value = "content";
        Duration duration = Duration.ofMillis(3000);
        redisUtil.setValues(key, value, duration);

        // when
        String result = redisUtil.getValues(key);

        // then
        assertThat(value).isEqualTo(result);

    }

    @DisplayName("Redis 저장 된 키 목록 불러오기 테스트")
    @Test
    void redis_get_keys() {
        // given
        Duration duration = Duration.ofMillis(500000);
        redisUtil.setValues("kal", "long", duration);
        redisUtil.setValues("kuku", "dal", duration);
        redisUtil.setValues("loka", "se", duration);

        // when
        List<String> keys = redisUtil.getMultiKeys("k");

        // then
        if (!keys.isEmpty() || keys != null) {
            for(String key : keys) {
                log.info("key info : {}", key);
            }
        }

    }

    @DisplayName("Redis 데이터 저장 이후 테스트")
    @Nested
    class redis_data_test{
        @BeforeEach
        void setUp() {
            redisUtil.setValues(KEY, VALUE, DURATION);
        }

        @AfterEach
        void tearDown() {
            redisUtil.deleteValues(KEY);
        }

        @DisplayName("Redis 자장된 데이터 조회 테스트")
        @Test
        void redis_save_data_test() {
        }

        @DisplayName("Redis 데이터 수정 테스트")
        @Test
        void redis_update_data_test() {
        }

        @DisplayName("Redis 데이터 삭제 테스트")
        @Test
        void redis_delete_data_test() {
        }
    }
}