package net.n2oapp.framework.test;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.boot.N2oSqlAutoConfiguration;
import net.n2oapp.framework.boot.route.jdbc.RedisRouteRepository;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import redis.embedded.RedisServer;

import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "n2o.config.register.store-type=redis",
                "n2o.config.register.redis.key=test_routes"
        })
@EnableAutoConfiguration(exclude = N2oSqlAutoConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RedisRouteRepositoryTest {

    @LocalServerPort
    private int appPort;

    @Autowired
    private RedisRouteRepository repository;

    @Autowired
    private RouteRegister routeRegister;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${n2o.config.register.redis.key}")
    private String HASH_KEY;

    private RedisServer redisServer;


    @BeforeEach
    public void setUp() {
        try {
            redisServer = RedisServer.builder().port(appPort).build();
            redisServer.start();
        } catch (Exception e) {
            //do nothing
        }
    }

    @AfterEach
    public void tearDown() {
        redisServer.stop();
    }

    @Test
    public void testRepositorySynchronize() {
        assertThat(repository, notNullValue());
        assertThat(routeRegister, notNullValue());
        routeRegister.clearAll();

        assertThat(getRouterSize(), is(0));
        assertThat(getRecordCount(), is(0));

        String testUrl = "/test/route";
        CompileContext context = new PageContext("page");

        // cинхронизация после добавления в routeRegister
        RouteInfoKey testRouteKey = new RouteInfoKey(testUrl, context.getCompiledClass());
        routeRegister.addRoute(testUrl, context);
        assertThat(getRouterSize(), is(1));
        assertThat(getRecordCount(), is(1));
        assertThat(repository.getAll().get(testRouteKey), instanceOf(PageContext.class));

        // повторное добавление через routeRegister
        routeRegister.addRoute(testUrl, context);
        assertThat(getRouterSize(), is(1));
        assertThat(getRecordCount(), is(1));

        // очистка
        routeRegister.clearAll();
        assertThat(getRouterSize(), is(0));
        assertThat(getRecordCount(), is(0));
        routeRegister.synchronize();
        assertThat(getRouterSize(), is(0));
        assertThat(getRecordCount(), is(0));

        // добавление в repository и синхронизация
        context = new ObjectContext("object");
        testRouteKey = new RouteInfoKey(testUrl, context.getCompiledClass());
        repository.save(testRouteKey, context);
        assertThat(getRouterSize(), is(0));
        assertThat(getRecordCount(), is(1));
        assertThat(repository.getAll().get(testRouteKey), instanceOf(ObjectContext.class));
        routeRegister.synchronize();
        assertThat(getRouterSize(), is(1));
        assertThat(getRecordCount(), is(1));

        // очистка repository и синхронизация
        repository.clearAll();
        assertThat(getRouterSize(), is(1));
        assertThat(getRecordCount(), is(0));
        routeRegister.synchronize();
        assertThat(getRouterSize(), is(1));
        assertThat(getRecordCount(), is(1));
    }

    private Integer getRecordCount() {
        return redisTemplate.opsForHash().size(HASH_KEY).intValue();
    }

    private Integer getRouterSize() {
        int i = 0;
        Iterator iter = routeRegister.iterator();
        while (iter.hasNext()) {
            iter.next();
            i++;
        }
        return i;
    }
}
