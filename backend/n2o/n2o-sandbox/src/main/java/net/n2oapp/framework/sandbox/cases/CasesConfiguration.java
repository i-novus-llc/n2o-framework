package net.n2oapp.framework.sandbox.cases;

import net.n2oapp.framework.sandbox.cases.cars.Car;
import net.n2oapp.framework.sandbox.cases.persons.Person;
import net.n2oapp.framework.sandbox.cases.theater.Actor;
import net.n2oapp.framework.sandbox.cases.theater.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class CasesConfiguration implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Car.class, Person.class, Actor.class, Role.class);
    }
}
