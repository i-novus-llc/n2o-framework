package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;

public class RouteAlreadyExistsException extends RuntimeException {

    public RouteAlreadyExistsException(String urlPattern, Class<? extends Compiled> compiledClass) {
        super(compiledClass.getSimpleName() + " by url '" + urlPattern + "' is already exists!");
    }
}
