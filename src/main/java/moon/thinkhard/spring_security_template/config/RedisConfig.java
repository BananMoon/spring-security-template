package moon.thinkhard.spring_security_template.config;

import io.lettuce.core.ReadFrom;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.time.Duration;

@Configuration
@EnableRedisRepositories

public class RedisConfig {
    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // connection pool 설정 필요함.
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        config.setPassword(RedisPassword.of(redisProperties.getPassword()));

        // pool config set : 트랜잭션 명령어를 사용한다면, 세팅한다.
        GenericObjectPoolConfig<?> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(10000);     // max connection
        poolConfig.setMaxIdle(8);      // max idle connection
        poolConfig.setMaxWait(Duration.ofMillis(1800));     // pool에서 커넥션 얻을 때까지 최대 대기 시간. 음수는 무한 대기.

        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
