package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Page;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Тестирование поиска метаданных по url
 */
public class RouterTest {

    @Test
    public void get_single() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        N2oRouter router = new N2oRouter(register, new MockBindPipeline(register));

        CompileContext<Page, ?> res = router.get("/", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("p"));

        res = router.get("/p/w", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pw"));
    }

    @Test
    public void get_coincidence() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, CompiledQuery.class));
        N2oRouter router = new N2oRouter(register, new MockBindPipeline(register));

        CompileContext<CompiledQuery, ?> res = router.get("/p/w", CompiledQuery.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getCompiledClass(), equalTo(CompiledQuery.class));

        CompileContext<Page, ?> res2 = router.get("/p/w", Page.class, null);
        assertThat(res2, notNullValue());
        assertThat(res2.getCompiledClass(), equalTo(Page.class));
    }

    @Test
    public void get_similar() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/p/:id", new MockCompileContext<>("/p/:id", "p", null, Page.class));
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        N2oRouter router = new N2oRouter(register, new MockBindPipeline(register));

        CompileContext<Page, ?> res = router.get("/p/w", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pw"));

        res = router.get("/p/123", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("p"));
    }

    @Test
    public void get_repair() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(register);
        N2oRouter router = new N2oRouter(register, pipeline);

        CompileContext<Page, ?> res;
        try {
            router.get("/p/w", Page.class, null);
            assert false;
        } catch (RouteNotFoundException ignore) {
        }

        pipeline.mock("p", (r, p) -> r.addRoute("/p/w", new MockCompileContext<>("/p/w", "w", null, Page.class)));
        res = router.get("/p/w", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("w"));
    }

    @Test
    public void get_repair2() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(register);
        N2oRouter router = new N2oRouter(register, pipeline);
        pipeline.mock("p", (r, p) -> {
            r.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, CompiledQuery.class));
            r.addRoute("/p/w/:id/a", new MockCompileContext<>("/p/w/:id/a", "pwa", null, Page.class));
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
    public void get_repair3() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(register);
        N2oRouter router = new N2oRouter(register, pipeline);
        pipeline.mock("p", (r, p) -> {
            r.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        });
        pipeline.mock("pw", (r, p) -> {
            r.addRoute("/p/w/:id", new MockCompileContext<>("/p/w/:id", "pw", null, CompiledQuery.class));
            r.addRoute("/p/w/:id/a", new MockCompileContext<>("/p/w/:id/a", "pwa", null, Page.class));
        });
        CompileContext<Page, ?> res;

        res = router.get("/p/w/123/a", Page.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pwa"));
    }

    @Test
    public void get_repair4() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/p", new MockCompileContext<>("/p", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(register);
        N2oRouter router = new N2oRouter(register, pipeline);
        pipeline.mock("p", (r, p) -> {
                r.addRoute("/p", new MockCompileContext<>("/p", "q", null, CompiledQuery.class));
        });
        CompileContext<CompiledQuery, ?> res;

        res = router.get("/p", CompiledQuery.class, null);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("q"));
    }

    @Test
    public void get_repairDynamic() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/", new MockCompileContext<>("/p", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(register);
        N2oRouter router = new N2oRouter(register, pipeline);
        pipeline.mock("p", (r, p) -> {
            r.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        });
        pipeline.mock("pw", (r, p) -> {
            if (p.get("id").equals("1")) {
                r.addRoute("/p/w/1/a", new MockCompileContext<>("/p/w/1/a", "pwa1", null, Page.class));
                r.addRoute("/p/w/1/a", new MockCompileContext<>("/p/w/1/a", "pwa1", null, CompiledQuery.class));
            }
            if (p.get("id").equals("2")) {
                r.addRoute("/p/w/2/a", new MockCompileContext<>("/p/w/2/a", "pwa2", null, Page.class));
                r.addRoute("/p/w/2/a", new MockCompileContext<>("/p/w/2/a", "pwa2", null, CompiledQuery.class));
            }
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
}