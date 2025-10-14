package net.n2oapp.framework.sandbox.service;

import net.n2oapp.framework.boot.N2oMongoAutoConfiguration;
import net.n2oapp.framework.boot.stomp.N2oWebSocketController;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"net.n2oapp.framework.sandbox.service", "net.n2oapp.framework.migrate"})
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class,
        N2oMongoAutoConfiguration.class,
        MongoAutoConfiguration.class
})
public class SandboxTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SandboxTestApplication.class, args);
    }

    @Bean
    public N2oWebSocketController n2oWebSocketController() {
        return Mockito.mock(N2oWebSocketController.class);
    }
}
