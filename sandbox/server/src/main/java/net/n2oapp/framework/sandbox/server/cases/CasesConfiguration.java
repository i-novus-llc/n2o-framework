package net.n2oapp.framework.sandbox.server.cases;

import net.n2oapp.framework.sandbox.server.cases.cars.Car;
import net.n2oapp.framework.sandbox.server.cases.persons.Person;
import net.n2oapp.framework.sandbox.server.cases.theater.Actor;
import net.n2oapp.framework.sandbox.server.cases.theater.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class CasesConfiguration implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Car.class, Person.class, Actor.class, Role.class);    }
}
