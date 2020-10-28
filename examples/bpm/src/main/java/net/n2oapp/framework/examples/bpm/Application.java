package net.n2oapp.framework.examples.bpm;

import org.camunda.bpm.engine.impl.cfg.IdGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean   //Костыль т.к. фронт падает из за - в id
    public IdGenerator getIdGenerator() {
        return () -> UUID.randomUUID().toString().replaceAll("-", "_");
    }

}
