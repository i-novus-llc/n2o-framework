package net.n2oapp.framework.test;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.boot.N2oSqlAutoConfiguration;
import net.n2oapp.framework.boot.route.jdbc.JDBCRouteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;

@SpringBootTest(properties = {"n2o.route.store-type=jdbc", "n2o.route.table-name=route_repository",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.datasource.username=sa",
        "spring.datasource.password=sa"})
@EnableAutoConfiguration(exclude = N2oSqlAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class JDBCRouteRepositoryTest {

    @Autowired
    JDBCRouteRepository repository;

    @Autowired
    RouteRegister routeRegister;

    @Test
    public void testRepositorySynchronize() {
        assertThat(repository, notNullValue());
        assertThat(routeRegister, notNullValue());

        int routeSize = getRouterSize(routeRegister);

        assertThat(routeSize, greaterThan(0));
        routeRegister.synchronize();

        assertThat(routeSize, greaterThan(0));
        assertThat(repository.getRecordCount(), equalTo(routeSize));
        Map<RouteInfoKey, CompileContext> result = repository.getAll();
        assertThat(result.size(), equalTo(routeSize));

        Map.Entry<RouteInfoKey, CompileContext> testRoute = result.entrySet().iterator().next();

        for (Map.Entry<RouteInfoKey, CompileContext> entry : result.entrySet()) {
            assertThat(entry.getKey().getCompiledClass(), notNullValue());
            assertThat(entry.getKey().getUrlMatching(), notNullValue());
            assertThat(entry.getValue(), notNullValue());
            assertThat(entry.getKey().getCompiledClass(), equalTo(entry.getValue().getCompiledClass()));

            if (testRoute.getKey().getUrlMatching().length() < entry.getKey().getUrlMatching().length())
                testRoute = entry;
        }

        String testUrl = testRoute.getKey().getUrlMatching();
        Class<? extends Compiled> testClass = testRoute.getKey().getCompiledClass();

        repository.clear(f -> f.getUrlMatching().equals(testUrl) && f.getCompiledClass().equals(testClass));
        assertThat(repository.getRecordCount(), equalTo(routeSize - 1));

        routeRegister.synchronize();    //синхронизация из routeRegister
        assertThat(repository.getRecordCount(), equalTo(routeSize));

        routeRegister.clear(testUrl);   //удаление из routeRegister и repository
        assertThat(repository.getRecordCount(), equalTo(routeSize - 1));
        assertThat(getRouterSize(routeRegister), equalTo(routeSize - 1));
        routeRegister.synchronize();
        assertThat(repository.getRecordCount(), equalTo(routeSize - 1));
        assertThat(getRouterSize(routeRegister), equalTo(routeSize - 1));

        routeRegister.addRoute(testUrl, testRoute.getValue()); //добавление из routeRegister
        assertThat(getRouterSize(routeRegister), equalTo(routeSize));
        assertThat(repository.getRecordCount(), equalTo(routeSize));

        routeRegister.clear(testUrl); //удаление из routeRegister и repository
        assertThat(repository.getRecordCount(), equalTo(routeSize - 1));
        assertThat(getRouterSize(routeRegister), equalTo(routeSize - 1));
        routeRegister.synchronize();
        assertThat(repository.getRecordCount(), equalTo(routeSize - 1));
        assertThat(getRouterSize(routeRegister), equalTo(routeSize - 1));

        repository.save(testRoute.getKey(), testRoute.getValue());
        assertThat(repository.getRecordCount(), equalTo(routeSize));
        assertThat(getRouterSize(routeRegister), equalTo(routeSize - 1));
        routeRegister.synchronize(); //добавление из repository
        assertThat(repository.getRecordCount(), equalTo(routeSize));
        assertThat(getRouterSize(routeRegister), equalTo(routeSize));
    }

    private int getRouterSize(Iterable iterable) {
        int i = 0;
        Iterator iter = iterable.iterator();
        while (iter.hasNext()) {
            iter.next();
            i++;
        }
        return i;
    }
}
