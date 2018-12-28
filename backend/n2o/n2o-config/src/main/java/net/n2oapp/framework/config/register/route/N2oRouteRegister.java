package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.api.register.route.RouteRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Хранилище RouteInfo
 */
public class N2oRouteRegister implements RouteRegister {
    private static final Logger logger = LoggerFactory.getLogger(N2oRouteRegister.class);

    private SortedSet<RouteInfo> register = new ConcurrentSkipListSet<>();

    @Override
    public void addRoute(RouteInfo routeInfo) {
        if (!routeInfo.getUrlMatching().startsWith("/"))
            throw new IncorrectRouteException(routeInfo.getUrlPattern());
        register.add(routeInfo);
        //todo throw RouteAlreadyExistsException if route exists
        logger.info(String.format("Register route: '%s' to [%s]", routeInfo.getContext(), routeInfo.getUrlPattern()));
    }

    @Override
    public Iterator<RouteInfo> iterator() {
        return register.iterator();
    }

    @Override
    public void clear(String startUrlMatching) {
        register.removeIf(s -> s.getUrlMatching().startsWith(startUrlMatching));
    }
}
