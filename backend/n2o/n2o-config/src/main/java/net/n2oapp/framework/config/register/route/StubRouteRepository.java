package net.n2oapp.framework.config.register.route;


import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class StubRouteRepository implements RouteRepository<RouteInfoKey, CompileContext> {

    @Override
    public CompileContext put(RouteInfoKey key, CompileContext value) {
        return value;
    }

    @Override
    public Map<RouteInfoKey, CompileContext> getAll() {
        return new HashMap<>();
    }

    @Override
    public void clear(Predicate<? super RouteInfoKey> filter) {
    }
}
