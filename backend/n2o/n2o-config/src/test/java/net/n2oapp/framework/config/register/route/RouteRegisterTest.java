package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.register.route.RouteRegister;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNotNull;

/**
 * Проверка добавления и удаления элементов из RouteRegister
 */
public class RouteRegisterTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAddRoute() throws Exception {
        RouteRegister register = getRegister();
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pW", null, Page.class));
        register.addRoute("/p/w/c/b", new MockCompileContext<>("/p/w/c/b", "pWcB", null, Page.class));
        register.addRoute("/p/w/c", new MockCompileContext<>("/p/w/c", "pWc", null, Page.class));
        String[] sortedUrl = new String[]{"/p/w/c/b", "/p/w/c", "/p/w"};
        for (String s : sortedUrl) {
            CompileContext result = register.find((k,v) -> k.getUrlMatching().equals(s));
            assertNotNull(result);
        }
    }

    @Test
    public void testRootRoute() {
        RouteRegister register = getRegister();
        register.addRoute("/", new MockCompileContext<>("/", "p1", null, Page.class));
        CompileContext result = register.find((k, v) -> k.getUrlMatching().equals("/"));
        assertNotNull(result);

        register = getRegister();
        register.addRoute("/:id", new MockCompileContext<>("/:id", "p1", null, Page.class));
        result = register.find((k, v) -> k.getUrlMatching().equals("/*"));
        assertNotNull(result);
    }

    @Test
    public void testAddRouteConflict() throws Exception {
        RouteRegister register = getRegister();
        register.addRoute("/a/:1", new MockCompileContext<>("/a/:1", "1", null, Page.class));
        try {
            register.addRoute("/a/:1", new MockCompileContext<>("/a/:1", "2", null, Page.class));
            Assert.fail();
        } catch (RouteAlreadyExistsException e) {
            assertThat(e.getMessage(), is("Page by url '/a/:1' is already exists!"));
        }
    }

    @Test
    public void testClear() throws Exception {
        RouteRegister register = getRegister();
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pW", null, Page.class));
        register.addRoute("/p/w/c/b", new MockCompileContext<>("/p/w/c/b", "pWcB", null, Page.class));
        register.addRoute("/p/w/c", new MockCompileContext<>("/p/w/c", "pWc", null, Page.class));
        register.clear("/p/w/c");

        CompileContext result = register.find((k, v) -> k.getUrlMatching().equals("/p/w"));
        assertNotNull(result);
    }

    private N2oRouteRegister getRegister() {
        return new N2oRouteRegister(new MapRouteRepository<>());
    }
}