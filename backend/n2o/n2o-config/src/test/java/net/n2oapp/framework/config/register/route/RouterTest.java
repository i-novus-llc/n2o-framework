package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.api.register.route.RoutingResult;
import org.junit.Before;
import org.junit.Test;

import static net.n2oapp.framework.config.register.route.RouteInfoUtil.createRouteInfo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Тестирование поиска метаданных по url
 */
public class RouterTest {
    private N2oRouter router;

    @Before
    public void setUp() throws Exception {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute(createRouteInfo("/", "p", Page.class));
        register.addRoute(createRouteInfo("/p/w1/:id", "pW1", Page.class));
        register.addRoute(createRouteInfo("/p/w/:id/c/b/:id2", "pWcB", Page.class));
        register.addRoute(createRouteInfo("/p/w/:id/c", "pWc", Page.class));
        register.addRoute(createRouteInfo("/p/w/:id/c", "pWcTable", Table.class));
        register.addRoute(createRouteInfo("/patients/:patients_id", "patients", Page.class));
        register.addRoute(createRouteInfo("/patients/create", "patientsCreate", Page.class));
        router = new N2oRouter(register, new MockBindPipeline(register));
    }

    @Test
    public void testGet() throws Exception {
        //получиние корневой метаданной
        RoutingResult rootRoute = router.get("/");
        assertNotNull(rootRoute.getContext(Page.class));
        //получение метаданной добавленной при старте
        RoutingResult pageRoute = router.get("/p/w/12/c");
        assertNotNull(pageRoute.getContext(Page.class));
        RoutingResult tableRoute = router.get("/p/w/12/c");
        assertNotNull(tableRoute.getContext(Table.class));
        //получение несуществующей метаданной
        try {
            router.get("/p/w/12/x");
            assert false;
        } catch (RouteNotFoundException e) {
        }
        //получение метаданной с пополнением регистра при компиляции
        pageRoute = router.get("/p/w/12/c/b");
        assertNotNull(pageRoute.getContext(Page.class));
        assertEquals(pageRoute.getContext(Page.class).getSourceId(null), "pWcB");
        pageRoute = router.get("/p/w1/12/c/b/name");
        assertNotNull(pageRoute.getContext(Page.class));
        assertEquals(pageRoute.getParams().get("id"), "12");
        assertEquals(pageRoute.getParams().get("name"), "name");
        pageRoute = router.get("/patients/create");
        assertNotNull(pageRoute.getContext(Page.class));
        assertEquals(pageRoute.getContext(Page.class).getSourceId(null), "patientsCreate");
    }
}