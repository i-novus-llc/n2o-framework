package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.event.MetadataChangedEvent;
import net.n2oapp.framework.api.event.N2oEventListener;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.register.route.RouteInfoKey;
import net.n2oapp.framework.api.register.route.RouteRegister;
import net.n2oapp.framework.config.register.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Хранилище RouteInfo
 */
public class N2oRouteRegister implements RouteRegister, N2oEventListener<MetadataChangedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(N2oRouteRegister.class);

    private final SortedMap<RouteInfoKey, CompileContext> register = new ConcurrentSkipListMap<>();
    private final ConfigRepository<RouteInfoKey, CompileContext> repository;

    public N2oRouteRegister() {
        this.repository = new StubRouteRepository();
    }

    public N2oRouteRegister(ConfigRepository<RouteInfoKey, CompileContext> repository) {
        this.repository = repository;
    }

    @Override
    public void addRoute(String urlPattern, CompileContext<? extends Compiled, ?> context) {
        if (register.isEmpty()) synchronize();

        RouteInfoKey key = new RouteInfoKey(urlPattern, context.getCompiledClass());
        if (!key.getUrlMatching().startsWith("/"))
            throw new IncorrectRouteException(key.getUrlMatching());
        CompileContext registeredContext = register.get(key);
        if (registeredContext == null) {
            register.put(key, context);
            repository.save(key, context);
        } else if (!registeredContext.equals(context))
            throw new RouteAlreadyExistsException(urlPattern, context.getCompiledClass());

        logger.info(String.format("Register route: '%s' to [%s]", context, urlPattern));
    }

    @Override
    public Iterator<Map.Entry<RouteInfoKey, CompileContext>> iterator() {
        return register.entrySet().iterator();
    }

    @Override
    public void clear(String startUrlMatching) {
        register.keySet().removeIf(s -> s.getUrlMatching().startsWith(startUrlMatching));
        repository.clear(s -> s.getUrlMatching().startsWith(startUrlMatching));
    }

    @Override
    public boolean synchronize() {
        Map<RouteInfoKey, CompileContext> stored = repository.getAll();
        for (Map.Entry<RouteInfoKey, CompileContext> entry : register.entrySet()) {
            if (!stored.containsKey(entry.getKey())) repository.save(entry.getKey(), entry.getValue());
        }

        boolean result = false;
        for (Map.Entry<RouteInfoKey, CompileContext> entry : stored.entrySet()) {
            register.put(entry.getKey(), entry.getValue());
            if (!register.containsKey(entry.getKey())) {
                result = true;
                logger.info(String.format("Register route from repository: '%s' to [%s]", entry.getValue(), entry.getKey().getUrlMatching()));
            }
        }
        return result;
    }

    @Override
    public void handleEvent(MetadataChangedEvent event) {
        this.clear("/");
    }
}
