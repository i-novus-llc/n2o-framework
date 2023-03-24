package net.n2oapp.framework.tutorial.crud_rest;

import net.n2oapp.framework.tutorial.crud_rest.model.Car;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@SpringBootApplication
public class Application implements RepositoryRestConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Car.class);
    }
}