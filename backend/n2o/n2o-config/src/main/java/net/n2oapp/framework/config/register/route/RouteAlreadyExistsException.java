package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.register.route.RouteInfo;

public class RouteAlreadyExistsException extends RuntimeException {

    public RouteAlreadyExistsException(RouteInfo info) {
        super(info.getContext().getCompiledClass().getSimpleName() + " by url '" + info.getUrlPattern() + "' is already exists!");
    }
}
