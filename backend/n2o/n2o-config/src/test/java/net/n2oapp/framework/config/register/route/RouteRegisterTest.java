package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.register.route.RouteInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Iterator;

import static net.n2oapp.framework.config.register.route.RouteInfoUtil.createRouteInfo;
import static org.junit.Assert.*;

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
        register.addRoute(createRouteInfo("/p/w", "pW", Page.class));
        register.addRoute(createRouteInfo("/p/w/c/b", "pWcB", Page.class));
        register.addRoute(createRouteInfo("/p/w/c", "pWc", Page.class));
        String[] sortedUrl = new String[] {"/p/w/c/b", "/p/w/c", "/p/w"};
        Iterator<RouteInfo> iter = register.iterator();
        for(int i = 0; iter.hasNext(); i++) {
            RouteInfo info = iter.next();
            assertEquals(sortedUrl[i], info.getUrlPattern());
        }
    }

    @Test
    public void testRootRoute() {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute(createRouteInfo("/", "p1", Page.class));
        RouteInfo info = register.iterator().next();
        assertEquals("/", info.getUrlPattern());
        assertEquals("/", info.getUrlMatching());

        register = new N2oRouteRegister();
        register.addRoute(createRouteInfo("/:id", "p1", Page.class));
        info = register.iterator().next();
        assertEquals("/:id", info.getUrlPattern());
        assertEquals("/*", info.getUrlMatching());
    }

    @Test
    @Ignore
    public void testAddRouteConflict() throws Exception {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute(createRouteInfo("/a/:1", "1", Page.class));
        try {
            register.addRoute(createRouteInfo("/a/:2", "2", Page.class));
            Assert.fail();
        } catch (RouteAlreadyExistsException ignored) {}
    }

    @Test
    public void testClear() throws Exception {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute(createRouteInfo("/p/w", "pW", Page.class));
        register.addRoute(createRouteInfo("/p/w/c/b", "pWcB",  Page.class));
        register.addRoute(createRouteInfo("/p/w/c", "pWc", Page.class));
        register.clear("/p/w/c");
        String[] sortedUrl = new String[] {"/p/w"};
        Iterator<RouteInfo> iter = register.iterator();
        for(int i = 0; iter.hasNext(); i++) {
            RouteInfo info = iter.next();
            assertEquals(sortedUrl[i], info.getUrlPattern());
        }
    }
}