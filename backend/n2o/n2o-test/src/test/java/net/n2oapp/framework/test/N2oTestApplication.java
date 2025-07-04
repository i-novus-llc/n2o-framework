package net.n2oapp.framework.test;

import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.boot.N2oMongoAutoConfiguration;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class, N2oMongoAutoConfiguration.class,
        MongoAutoConfiguration.class})
public class N2oTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(N2oTestApplication.class, args);
    }

    @Bean
    public ProcessEngine processEngine() {
        return null;
    }

    @Bean
    @Primary
    public OperationExceptionHandler testOperationExceptionHandler() {
        return new TestOperationExceptionHandler();
    }
}
