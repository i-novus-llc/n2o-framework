package net.n2oapp.framework.config.register.route;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Тестирование поиска метаданных по url
 */
public class RouterTest {
    private N2oRouter router;
    private N2oRouteRegister register;

    @Before
    public void setUp() throws Exception {
        N2oRouteRegister register = new N2oRouteRegister();
        register.addRoute("/", new MockCompileContext<>("/", "p", null, Page.class));
        register.addRoute("/p/w1/:id", new MockCompileContext<>("/p/w1/:id", "pW1", null, Page.class));
        register.addRoute("/p/w/:id/c/b/:id2", new MockCompileContext<>("/p/w/:id/c/b/:id2", "pWcB", null, Page.class));
        register.addRoute("/p/w/:id/c/b", new MockCompileContext<>("/p/w/:id/c/b", "pWcQuery", null, CompiledQuery.class));
        register.addRoute("/p/w/:id/c", new MockCompileContext<>("/p/w/:id/c", "pWc", null, Page.class));
        register.addRoute("/p/w/:id/c", new MockCompileContext<>("/p/w/:id/c", "pWcTable", null, Table.class));
        register.addRoute("/patients/:patients_id", new MockCompileContext<>("/patients/:patients_id", "patients", null, Page.class));
        register.addRoute("/patients/create", new MockCompileContext<>("/patients/create", "patientsCreate", null, Page.class));
        this.register = register;
        router = new N2oRouter(register, new MockBindPipeline(register));
    }

    @Test
    public void testGet() throws Exception {
        //получиние корневой метаданной
        assertNotNull(router.get("/", Page.class));
        //получение метаданной добавленной при старте
        assertNotNull(router.get("/p/w/12/c", Page.class));
        assertNotNull(router.get("/p/w/12/c", Table.class));
        //получение несуществующей метаданной
        try {
            router.get("/p/w/12/x", Page.class);
            assert false;
        } catch (RouteNotFoundException e) {
        }
        //получение метаданной с пополнением регистра при компиляции
        assertNotNull(router.get("/p/w/12/c/b", Page.class));
        assertEquals(router.get("/p/w/12/c/b", Page.class).getSourceId(null), "pWcB");
        CompileContext<Page, ?> context1 = router.get("/p/w1/12/c/b/name", Page.class);
        assertNotNull(context1);
        DataSet params = context1.getParams("/p/w1/12/c/b/name", null);
        assertEquals(params.get("id"), "12");
        assertEquals(params.get("name"), "name");
        assertNotNull(router.get("/patients/create",Page.class));
        assertEquals(router.get("/patients/create",Page.class).getSourceId(null), "patientsCreate");

        //проверяется, что новый контекст заменяет старый
        MockCompileContext context = (MockCompileContext) router.get("/p/w/12/c/b", Page.class);
        assertNull(context.getParentModelLink());
        context.setParentModelLink(new ModelLink(null));
        register.addRoute("/p/w/:id/c/b/:id2", context);
        assertNotNull(((MockCompileContext<Page, ?>) router.get("/p/w/12/c/b", Page.class)).getParentModelLink());
    }
}