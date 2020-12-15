package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * Хранилище RouteInfo
 */
public class N2oRouteRegister implements RouteRegister {
    private static final Logger logger = LoggerFactory.getLogger(N2oRouteRegister.class);

    private final RouteRepository<RouteInfoKey, CompileContext> repository;

    public N2oRouteRegister(RouteRepository<RouteInfoKey, CompileContext> repository) {
        this.repository = repository;
    }

    @Override
    public void addRoute(String urlPattern, CompileContext<? extends Compiled, ?> context) {
        RouteInfoKey key = new RouteInfoKey(urlPattern, context.getCompiledClass());
        if (!key.getUrlMatching().startsWith("/"))
            throw new IncorrectRouteException(key.getUrlMatching());
        CompileContext registeredContext = repository.get(key);
        if (registeredContext == null) {
            repository.put(key, context);
        } else if (!registeredContext.equals(context))
            throw new RouteAlreadyExistsException(urlPattern, context.getCompiledClass());

        logger.info(String.format("Register route: '%s' to [%s]", context, urlPattern));
    }

    @Override
    public Iterator<Map.Entry<RouteInfoKey, CompileContext>> iterator() {
        return repository.iterator();
    }

    @Override
    public void clear(String startUrlMatching) {
        repository.clear(s -> s.getUrlMatching().startsWith(startUrlMatching));
    }
}
