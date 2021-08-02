package net.n2oapp.framework.test;

import net.n2oapp.framework.api.data.OperationExceptionHandler;
import net.n2oapp.framework.boot.sql.jdbc.EnableRoutingDataSource;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@EnableAutoConfiguration
@EnableRoutingDataSource
@ComponentScan
public class N2oTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(N2oTestApplication.class, args);
    }

    @Bean
    @Primary
    public OperationExceptionHandler operationExceptionHandler() {
        return new TestOperationExceptionHandler();
    }

    @Bean
    public ProcessEngine processEngine() {
        return null;
    }

}
