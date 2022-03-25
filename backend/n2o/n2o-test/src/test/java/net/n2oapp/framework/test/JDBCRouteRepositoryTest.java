package net.n2oapp.framework.test;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.boot.N2oSqlAutoConfiguration;
import net.n2oapp.framework.boot.route.jdbc.JDBCRouteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(properties = {"n2o.config.register.store-type=jdbc", "n2o.config.register.jdbc.create-table=true",
        "n2o.config.register.jdbc.table-name=route_repository",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.datasource.username=sa",
        "spring.datasource.password=sa",
        "spring.jpa.defer-datasource-initialization=true",
        "spring.datasource.initialization-mode=always"})
@EnableAutoConfiguration(exclude = N2oSqlAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class JDBCRouteRepositoryTest {

    @Autowired
    JDBCRouteRepository repository;

    @Autowired
    RouteRegister routeRegister;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testRepositorySynchronize() {
        assertThat(repository, notNullValue());
        assertThat(routeRegister, notNullValue());

        int routeSize = getRouterSize();

        assertThat(routeSize, greaterThan(0));
        routeRegister.synchronize();

        assertThat(routeSize, greaterThan(0));
        assertThat(getRecordCount(), equalTo(routeSize));
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

        routeRegister.addRoute(testUrl, testRoute.getValue()); //повторное добавление ч.з. routeRegister
        assertThat(getRouterSize(), equalTo(routeSize));
        assertThat(getRecordCount(), equalTo(routeSize));

        repository.clearAll();    //удаление из репозитория
        assertThat(getRecordCount(), equalTo(0));
        routeRegister.synchronize();    //синхронизация из routeRegister
        assertThat(getRecordCount(), equalTo(routeSize));

        routeRegister.clearAll();   //удаление из routeRegister и repository
        assertThat(getRecordCount(), equalTo(0));
        assertThat(getRouterSize(), equalTo(0));
        routeRegister.synchronize();
        assertThat(getRecordCount(), equalTo(0));
        assertThat(getRouterSize(), equalTo(0));

        routeRegister.addRoute(testUrl, testRoute.getValue()); //добавление из routeRegister
        assertThat(getRouterSize(), equalTo(1));
        assertThat(getRecordCount(), equalTo(1));

        routeRegister.clearAll(); //удаление из routeRegister и repository
        assertThat(getRecordCount(), equalTo(0));
        assertThat(getRouterSize(), equalTo(0));

        repository.save(testRoute.getKey(), testRoute.getValue());
        assertThat(getRecordCount(), equalTo(1));
        assertThat(getRouterSize(), equalTo(0));
        routeRegister.synchronize(); //добавление из repository
        assertThat(getRecordCount(), equalTo(1));
        assertThat(getRouterSize(), equalTo(1));
    }

    private Integer getRecordCount() {
        String selectSQL = "SELECT count(id) FROM route_repository";

        return jdbcTemplate.queryForObject(selectSQL, Integer.class);
    }

    private int getRouterSize() {
        int i = 0;
        Iterator iter = routeRegister.iterator();
        while (iter.hasNext()) {
            iter.next();
            i++;
        }
        return i;
    }
}
