package net.n2oapp.framework.config.register.route;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import org.junit.Before;
import org.junit.Test;

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

        CompileContext<Page, ?> res = router.get("/", Page.class);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("p"));

        res = router.get("/p/w", Page.class);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pw"));
    }

    @Test
    public void get_coincidence() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, CompiledQuery.class));
        N2oRouter router = new N2oRouter(register, new MockBindPipeline(register));

        CompileContext<CompiledQuery, ?> res = router.get("/p/w", CompiledQuery.class);
        assertThat(res, notNullValue());
        assertThat(res.getCompiledClass(), equalTo(CompiledQuery.class));

        CompileContext<Page, ?> res2 = router.get("/p/w", Page.class);
        assertThat(res2, notNullValue());
        assertThat(res2.getCompiledClass(), equalTo(Page.class));
    }

    @Test
    public void get_similar() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/p/:id", new MockCompileContext<>("/p/:id", "p", null, Page.class));
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, Page.class));
        N2oRouter router = new N2oRouter(register, new MockBindPipeline(register));

        CompileContext<Page, ?> res = router.get("/p/w", Page.class);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pw"));

        res = router.get("/p/123", Page.class);
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
            router.get("/p/w", Page.class);
            assert false;
        } catch (RouteNotFoundException ignore) {
        }

        pipeline.mock("p", (r) -> r.addRoute("/p/w", new MockCompileContext<>("/p/w", "w", null, Page.class)));
        res = router.get("/p/w", Page.class);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("w"));
    }

    @Test
    public void get_repair2() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        MockBindPipeline pipeline = new MockBindPipeline(register);
        N2oRouter router = new N2oRouter(register, pipeline);
        pipeline.mock("p", (r) -> {
            r.addRoute("/p/w", new MockCompileContext<>("/p/w", "pw", null, CompiledQuery.class));
            r.addRoute("/p/w/:id/a", new MockCompileContext<>("/p/w/:id/a", "pwa", null, Page.class));
        });
        CompileContext<Page, ?> res;

        res = router.get("/p/w/123/a", Page.class);
        assertThat(res, notNullValue());
        assertThat(res.getSourceId(null), is("pwa"));

        try {
            router.get("/p/w/123", Page.class);
            assert false;
        } catch (RouteNotFoundException ignore) {
        }

        try {
            router.get("/p/w", Page.class);
            assert false;
        } catch (RouteNotFoundException ignore) {
        }

        try {
            router.get("/p", Page.class);
            assert false;
        } catch (RouteNotFoundException ignore) {
        }
    }
}