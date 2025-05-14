package net.n2oapp.framework.test;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import redis.embedded.RedisServer;

@TestConfiguration
@Import(RedisAutoConfiguration.class)
public class TestRedisConfiguration {
    private RedisServer redisServer;

    public TestRedisConfiguration(@Value("${spring.data.redis.port}") int port) {
        this.redisServer = RedisServer.builder()
                .port(port)
                .setting("maxmemory 128M")
                .build();
    }

    @PostConstruct
    public void postConstruct() {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }
}
