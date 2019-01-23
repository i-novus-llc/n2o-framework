package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteInfoValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Проверка добавления и удаления элементов из RouteRegister
 */
public class RouteRegisterTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAddRoute() throws Exception {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/p/w", new MockCompileContext<>("pW", null, Page.class));
        register.addRoute("/p/w/c/b", new MockCompileContext<>("pWcB", null, Page.class));
        register.addRoute("/p/w/c", new MockCompileContext<>("pWc", null, Page.class));
        String[] sortedUrl = new String[]{"/p/w/c/b", "/p/w/c", "/p/w"};
        Iterator<Map.Entry<RouteInfoKey, RouteInfoValue>> iter = register.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            Map.Entry<RouteInfoKey, RouteInfoValue> info = iter.next();
            assertEquals(sortedUrl[i], info.getValue().getUrlPattern());
        }
    }

    @Test
    public void testRootRoute() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/", new MockCompileContext<>("p1", null, Page.class));
        Map.Entry<RouteInfoKey, RouteInfoValue> info = register.iterator().next();
        assertEquals("/", info.getValue().getUrlPattern());
        assertEquals("/", info.getKey().getUrlMatching());
        register = new N2oRouteRegister();
        register.addRoute("/:id", new MockCompileContext<>("p1", null, Page.class));
        info = register.iterator().next();
        assertEquals("/:id", info.getValue().getUrlPattern());
        assertEquals("/*", info.getKey().getUrlMatching());
    }

    @Test
    public void testAddRouteConflict() throws Exception {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/a/:1", new MockCompileContext<>("1", null, Page.class));
        try {
            register.addRoute("/a/:1", new MockCompileContext<>("2", null, Page.class));
            Assert.fail();
        } catch (RouteAlreadyExistsException e) {
            assertThat(e.getMessage(), is("Page by url '/a/:1' is already exists!"));
        }
    }

    @Test
    public void testClear() throws Exception {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/p/w", new MockCompileContext<>("pW", null, Page.class));
        register.addRoute("/p/w/c/b", new MockCompileContext<>("pWcB", null, Page.class));
        register.addRoute("/p/w/c", new MockCompileContext<>("pWc", null, Page.class));
        register.clear("/p/w/c");
        String[] sortedUrl = new String[]{"/p/w"};
        Iterator<Map.Entry<RouteInfoKey, RouteInfoValue>> iter = register.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            Map.Entry<RouteInfoKey, RouteInfoValue> info = iter.next();
            assertEquals(sortedUrl[i], info.getValue().getUrlPattern());
        }
    }
}