package moon.thinkhard.spring_security_template.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    private final Log logger;

    private final StringRedisTemplate redisTemplate;


    public RedisUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.logger = LogFactory.getLog(getClass());
    }

    public boolean save(String key, String value, long ttl, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl, timeUnit);
        } catch (Exception ex) {
            logger.error(ex);
        }
        logger.info("✅ Redis에 값 저장: " + key + " -> " + value);

        return true;
    }

    public String findBy(String key) {
        String storedValue = redisTemplate.opsForValue().get(key);
        logger.info("🔍 Redis에서 조회한 값: " + storedValue);

        return storedValue;
    }
}
