package net.n2oapp.framework.sandbox.server.view;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.config.register.route.N2oRouteRegister;
import net.n2oapp.framework.sandbox.server.engine.thread_local.ThreadLocalProjectId;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Обработчик роутов для разный проектов
 */
@Component
public class ProjectRouteRegister implements RouteRegister {

    private final Map<String, RouteRegister> register = new ConcurrentHashMap<>();

    @Override
    public void addRoute(String urlPattern, CompileContext<? extends Compiled, ?> context) {
        getProjectRegister().addRoute(urlPattern, context);
    }

    @Override
    public void clearAll() {
        getProjectRegister().clearAll();
    }

    @Override
    public Iterator<Map.Entry<RouteInfoKey, CompileContext>> iterator() {
        return getProjectRegister().iterator();
    }

    private RouteRegister getProjectRegister() {
        return register.computeIfAbsent(ThreadLocalProjectId.getProjectId(), projectId -> new N2oRouteRegister());
    }
}
