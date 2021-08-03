package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.config.register.ConfigRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.Matchers.is;
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
        RouteRegister register = getRegister();
        register.addRoute("/p/w", new MockCompileContext<>("/p/w", "pW", null, Page.class));
        register.addRoute("/p/w/c/b", new MockCompileContext<>("/p/w/c/b", "pWcB", null, Page.class));
        register.addRoute("/p/w/c", new MockCompileContext<>("/p/w/c", "pWc", null, Page.class));
        String[] sortedUrl = new String[]{"/p/w/c/b", "/p/w/c", "/p/w"};
        Iterator<Map.Entry<RouteInfoKey, CompileContext>> iter = register.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            Map.Entry<RouteInfoKey, CompileContext> info = iter.next();
            assertEquals(sortedUrl[i], info.getKey().getUrlMatching());
        }
    }

    @Test
    public void testRootRoute() {
        RouteRegister register = getRegister();
        register.addRoute("/", new MockCompileContext<>("/", "p1", null, Page.class));
        Map.Entry<RouteInfoKey, CompileContext> info = register.iterator().next();
        assertEquals("/", info.getKey().getUrlMatching());
        register = getRegister();
        register.addRoute("/:id", new MockCompileContext<>("/:id", "p1", null, Page.class));
        info = register.iterator().next();
        assertEquals("/*", info.getKey().getUrlMatching());
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
        register.clearAll();
        assertFalse(register.iterator().hasNext());
    }

    @Test
    public void testRouteRepository() {
        TestRouteRepository repository = new TestRouteRepository();
        RouteRegister register = new N2oRouteRegister(repository);

        MockCompileContext<Page, Object> context = new MockCompileContext<>("/p/w", "pW", null, Page.class);
        RouteInfoKey key = new RouteInfoKey("/p/w", context.getCompiledClass());
        repository.save(key, context);

        context = new MockCompileContext<>("/p/w/c/b", "pWcB", null, Page.class);
        key = new RouteInfoKey("/p/w/c/b", context.getCompiledClass());
        repository.save(key, context);

        assertFalse(register.iterator().hasNext());
        assertEquals(repository.getAll().size(), 2);

        register.addRoute("/p/w/c", new MockCompileContext<>("/p/w/c", "pWc", null, Page.class));
        String[] sortedUrl = new String[]{"/p/w/c/b", "/p/w/c", "/p/w"};
        Iterator<Map.Entry<RouteInfoKey, CompileContext>> iter = register.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            Map.Entry<RouteInfoKey, CompileContext> info = iter.next();
            assertEquals(sortedUrl[i], info.getKey().getUrlMatching());
        }

        assertEquals(repository.getAll().size(), 3);
    }

    private RouteRegister getRegister() {
        return new N2oRouteRegister(new TestRouteRepository());
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
            repository.clear();
        }
    }
}