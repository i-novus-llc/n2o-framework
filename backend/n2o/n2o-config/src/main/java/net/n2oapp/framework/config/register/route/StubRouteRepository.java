package net.n2oapp.framework.config.register.route;


import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.config.register.ConfigRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Заглушка для RouteRepository (не хранит данные)
 */
public class StubRouteRepository implements ConfigRepository<RouteInfoKey, CompileContext> {

    @Override
    public CompileContext save(RouteInfoKey key, CompileContext value) {
        return value;
    }

    @Override
    public Map<RouteInfoKey, CompileContext> getAll() {
        return new HashMap<>();
    }

    @Override
    public void clearAll() {
    }
}
