package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Хранилище RouteInfo
 */
public class N2oRouteRegister implements RouteRegister {
    private static final Logger logger = LoggerFactory.getLogger(N2oRouteRegister.class);

    private final SortedMap<RouteInfoKey, CompileContext> register = new ConcurrentSkipListMap<>();

    @Override
    public void addRoute(String urlPattern, CompileContext<? extends Compiled, ?> context) {
        RouteInfoKey key = new RouteInfoKey(urlPattern, context.getCompiledClass());
        if (!key.getUrlMatching().startsWith("/"))
            throw new IncorrectRouteException(key.getUrlMatching());
        if (register.containsKey(key) && !register.get(key).equals(context))
            throw new RouteAlreadyExistsException(urlPattern, context.getCompiledClass());
        register.put(key, context);

        logger.info(String.format("Register route: '%s' to [%s]", context, urlPattern));
    }

    @Override
    public Iterator<Map.Entry<RouteInfoKey, CompileContext>> iterator() {
        return register.entrySet().iterator();
    }

    @Override
    public void clear(String startUrlMatching) {
        register.keySet().removeIf(s -> s.getUrlMatching().startsWith(startUrlMatching));
    }
}
