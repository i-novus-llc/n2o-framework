package net.n2oapp.framework.sandbox.service;

import net.n2oapp.framework.boot.N2oMongoAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class, N2oMongoAutoConfiguration.class,
        MongoAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class})
public class SandboxTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandboxTestApplication.class, args);
    }

}
