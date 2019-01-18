package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.register.route.RouteInfo;
import net.n2oapp.framework.api.register.route.RouteRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Хранилище RouteInfo
 */
public class N2oRouteRegister implements RouteRegister {
    private static final Logger logger = LoggerFactory.getLogger(N2oRouteRegister.class);

    private final SortedMap<RouteInfo, RouteInfo> register = new ConcurrentSkipListMap<>();

    @Override
    public void addRoute(RouteInfo routeInfo) {
        if (!routeInfo.getUrlMatching().startsWith("/"))
            throw new IncorrectRouteException(routeInfo.getUrlPattern());
        if (register.containsKey(routeInfo) && !register.get(routeInfo).getContext().equals(routeInfo.getContext()))
            throw new RouteAlreadyExistsException(routeInfo);
        register.put(routeInfo, routeInfo);

        logger.info(String.format("Register route: '%s' to [%s]", routeInfo.getContext(), routeInfo.getUrlPattern()));
    }

    @Override
    public Iterator<RouteInfo> iterator() {
        return register.values().iterator();
    }

    @Override
    public void clear(String startUrlMatching) {
        register.keySet().removeIf(s -> s.getUrlMatching().startsWith(startUrlMatching));
    }
}
