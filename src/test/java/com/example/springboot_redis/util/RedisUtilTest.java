package com.example.springboot_redis.util;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class RedisUtilTest {

    final String KEY = "key";
    final Long HASHKEY = 11L;
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

    @DisplayName("Redis String test")
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
            // when
            String result = redisUtil.getValues(KEY);

            // then
            assertThat(result).isEqualTo(VALUE);
        }

        @DisplayName("Redis 데이터 수정 테스트")
        @Test
        void redis_update_data_test() {
            // given
            String updateValue = "update";
            redisUtil.setValues(KEY, updateValue, DURATION);

            // when
            String result = redisUtil.getValues(KEY);

            // then
            assertThat(result).isEqualTo(updateValue);
            assertThat(result).isNotEqualTo(VALUE);
        }

        @DisplayName("Redis 데이터 삭제 테스트")
        @Test
        void redis_delete_data_test() {
            // when
            redisUtil.deleteValues(KEY);
            String findValue = redisUtil.getValues(KEY);

            // then
            assertThat(findValue).isEqualTo("false");
        }

        @DisplayName("Redis 설정한 유지시간에 대한 정상 동작 여부 테스트")
        @Test
        void redis_expired_data_test() {
            // when
            String findvalue = redisUtil.getValues(KEY);
            await().pollDelay(Duration.ofMillis(5000)).untilAsserted(
                    () -> {
                        String expiredValue = redisUtil.getValues(KEY);
                        assertThat(expiredValue).isNotEqualTo(findvalue);
                        assertThat(expiredValue).isEqualTo("false");
                    }
            );
        }

        @DisplayName("Redis 저장된 데이터 지속시간 설정")
        @Test
        void name() {
            redisUtil.expireValues("aaa", 10);
        }
    }

    @DisplayName("Redis Hash Test")
    @Nested
    class hash_test {

        @BeforeEach
        void setUp() {
            redisUtil.setHashValue(KEY, HASHKEY, VALUE);
        }

        @AfterEach
        void tearDown() {
            redisUtil.deleteHash(KEY, HASHKEY);
        }

        @DisplayName("Redis Hash 데이터 저장 테스트")
        @Test
        void redis_hash_data_save_test() {
            // given
            String key = "test";
            long hashKey = 1L;
            String value = "test";
            redisUtil.setHashValue(key, hashKey, value);

            // when
            String result = redisUtil.getHashValue(key, hashKey);

            // then
            assertThat(result).isEqualTo(value);
        }

        @DisplayName("Redis Hash 데이터 수정 테스트")
        @Test
        void redis_hash_data_update_test() {
            // given
            String updateValue = "update";
            redisUtil.setHashValue(KEY, HASHKEY, updateValue);

            // when
            String result = redisUtil.getHashValue(KEY, HASHKEY);

            //then
            assertThat(result).isEqualTo(updateValue);
            assertThat(result).isNotEqualTo(VALUE);

        }

        @DisplayName("Redis Hash 데이터 삭제 테스트")
        @Test
        void redis_hash_data_delete_test() {
            // given
            redisUtil.deleteHash(KEY, HASHKEY);

            // when
            String result = redisUtil.getHashValue(KEY, HASHKEY);

            // then
            assertThat(result).isEqualTo("false");
        }
    }
}