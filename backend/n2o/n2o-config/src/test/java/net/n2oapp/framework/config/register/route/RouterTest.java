package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.pack.N2oOperationsPack;
import net.n2oapp.framework.config.register.ConfigRepository;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование поиска метаданных по url
 */
class RouterTest {

    private N2oApplicationBuilder builder;

    @BeforeEach
    void setUp() {
        N2oEnvironment env = new N2oEnvironment();
        env.setSystemProperties(new SimplePropertyResolver(new Properties()));
        env.setNamespacePersisterFactory(new PersisterFactoryByMap());
        env.setNamespaceReaderFactory(new ReaderFactoryByMap());
        builder = new N2oApplicationBuilder(env).packs(new N2oOperationsPack());
    }

    @Test
    void get_single() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        env.getRouteRegister().addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        env.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        N2oRouter router = new N2oRouter(env, new MockBindPipeline(env));

        CompileContext<Page, ?> res = router.get("/", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("p"));

        res = router.get("/p/w", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pw"));
    }

    @Test
    void get_coincidence() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        env.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        env.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, CompiledQuery.class));
        N2oRouter router = new N2oRouter(env, new MockBindPipeline(env));

        CompileContext<CompiledQuery, ?> res = router.get("/p/w", CompiledQuery.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getCompiledClass(), equalTo(CompiledQuery.class));

        CompileContext<Page, ?> res2 = router.get("/p/w", Page.class, null);
        assertThat(res2, notNullValue());
        assertThat(res2.getCompiledClass(), equalTo(Page.class));
    }

    @Test
    void get_similar() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        env.getRouteRegister().addRoute("/p/:id", new MockCompileContext<>("/p/:id", "p", null, Page.class));
        env.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        N2oRouter router = new N2oRouter(env, new MockBindPipeline(env));

        CompileContext<Page, ?> res = router.get("/p/w", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pw"));

        res = router.get("/p/123", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("p"));
    }

    @Test
    void get_repair() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        env.getRouteRegister().addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(env);
        N2oRouter router = new N2oRouter(env, pipeline);

        CompileContext<Page, ?> res;
        try {
            router.get("/p/w", Page.class, null);
            assert false;
        } catch (RouteNotFoundException ignore) {
        }

        pipeline.mock("p", (r, p) -> r.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "w", null, Page.class)));
        res = router.get("/p/w", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("w"));
    }

    @Test
    void get_repair2() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        env.getRouteRegister().addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(env);
        N2oRouter router = new N2oRouter(env, pipeline);
        pipeline.mock("p", (r, p) -> {
            r.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, CompiledQuery.class));
            r.getRouteRegister().addRoute("/p/w/:id/a", new MockCompileContext<>("/p/w/:id/a", "pwa", null, Page.class));
        });
        CompileContext<Page, ?> res;

        res = router.get("/p/w/123/a", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pwa"));

        try {
            router.get("/p/w/123", Page.class, null);
            assert false;
        } catch (RouteNotFoundException ignore) {
        }

        try {
            router.get("/p/w", Page.class, null);
            assert false;
        } catch (RouteNotFoundException ignore) {
        }

        try {
            router.get("/p", Page.class, null);
            assert false;
        } catch (RouteNotFoundException ignore) {
        }
    }

    @Test
    void get_repair3() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        env.getRouteRegister().addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(env);
        N2oRouter router = new N2oRouter(env, pipeline);
        pipeline.mock("p", (r, p) -> {
            r.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        });
        pipeline.mock("pw", (r, p) -> {
            r.getRouteRegister().addRoute("/p/w/:id", new MockCompileContext<>("/p/w/:id", "pw", null, CompiledQuery.class));
            r.getRouteRegister().addRoute("/p/w/:id/a", new MockCompileContext<>("/p/w/:id/a", "pwa", null, Page.class));
        });
        CompileContext<Page, ?> res;

        res = router.get("/p/w/123/a", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pwa"));
    }

    @Test
    void get_repair4() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        env.getRouteRegister().addRoute("/p", new MockCompileContext<>("/p", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(env);
        N2oRouter router = new N2oRouter(env, pipeline);
        pipeline.mock("p", (r, p) -> {
            r.getRouteRegister().addRoute("/p", new MockCompileContext<>("/p", "q", null, CompiledQuery.class));
        });
        CompileContext<CompiledQuery, ?> res;

        res = router.get("/p", CompiledQuery.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("q"));
    }

    @Test
    void get_repairDynamic() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        env.getRouteRegister().addRoute("/", new MockCompileContext<>("/p", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(env);
        N2oRouter router = new N2oRouter(env, pipeline);
        pipeline.mock("p", (r, p) -> {
            r.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        });
        pipeline.mock("pw", (r, p) -> {
            r.getRouteRegister().addRoute("/p/w/1/a", new MockCompileContext<>("/p/w/1/a", "pwa1", null, Page.class));
            r.getRouteRegister().addRoute("/p/w/1/a", new MockCompileContext<>("/p/w/1/a", "pwa1", null, CompiledQuery.class));
            r.getRouteRegister().addRoute("/p/w/2/a", new MockCompileContext<>("/p/w/2/a", "pwa2", null, Page.class));
            r.getRouteRegister().addRoute("/p/w/2/a", new MockCompileContext<>("/p/w/2/a", "pwa2", null, CompiledQuery.class));

        });
        CompileContext<Page, ?> res;
        HashMap<String, String[]> params = new HashMap<>();
        params.put("id", new String[]{"1"});
        res = router.get("/p/w/1/a", Page.class, params);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pwa1"));
        CompileContext<CompiledQuery, ?> resQuery = router.get("/p/w/1/a", CompiledQuery.class, params);
        assertThat(resQuery, notNullValue());
        assertThat(resQuery.getSourceId(null), is("pwa1"));

        params.put("id", new String[]{"2"});
        res = router.get("/p/w/2/a", Page.class, params);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pwa2"));
        resQuery = router.get("/p/w/2/a", CompiledQuery.class, params);
        assertThat(resQuery, notNullValue());
        assertThat(resQuery.getSourceId(null), is("pwa2"));
    }

    @Test
    void get_root_application() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        ((SimplePropertyResolver) env.getSystemProperties()).setProperty("n2o.application.id", "header");
        MockBindPipeline pipeline = new MockBindPipeline(env);
        N2oRouter router = new N2oRouter(env, pipeline);
        pipeline.mock("header", (r, p) -> {
            r.getRouteRegister().addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
            r.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        });

        CompileContext<Page, ?> res = router.get("/p/w", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pw"));

        res = router.get("/", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("p"));
    }

    @Test
    void get_root_homepage() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        ((SimplePropertyResolver) env.getSystemProperties()).setProperty("n2o.application.id", "");
        ((SimplePropertyResolver) env.getSystemProperties()).setProperty("n2o.homepage.id", "index");
        MockBindPipeline pipeline = new MockBindPipeline(env);
        N2oRouter router = new N2oRouter(env, pipeline);
        pipeline.mock("index", (r, p) -> {
            r.getRouteRegister().addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
            r.getRouteRegister().addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        });

        CompileContext<Page, ?> res = router.get("/p/w", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pw"));

        res = router.get("/", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("p"));
    }

    @Test
    void route_repository() {
        N2oEnvironment env = (N2oEnvironment) builder.getEnvironment();
        TestRouteRepository repository = new TestRouteRepository();
        env.setRouteRegister(new N2oRouteRegister(repository));

        MockCompileContext<Page, Object> context = new MockCompileContext<>("/", "p", null, Page.class);
        RouteInfoKey key = new RouteInfoKey("/", context.getCompiledClass());
        repository.save(key, context);

        context = new MockCompileContext<>("/p/w", "pw", null, Page.class);
        key = new RouteInfoKey("/p/w", context.getCompiledClass());
        repository.save(key, context);

        N2oRouter router = new N2oRouter(env, new MockBindPipeline(env));

        CompileContext<Page, ?> res = router.get("/", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("p"));

        res = router.get("/p/w", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pw"));
    }


    private static class TestRouteRepository implements ConfigRepository<RouteInfoKey, CompileContext> {

        Map<RouteInfoKey, CompileContext> repository = new HashMap<>();

        @Override
        public CompileContext save(RouteInfoKey key, CompileContext value) {
            return repository.put(key, value);
        }

        @Override
        public Map<RouteInfoKey, CompileContext> getAll() {
            return repository;
        }

        @Override
        public void clearAll() {
        }
    }
}