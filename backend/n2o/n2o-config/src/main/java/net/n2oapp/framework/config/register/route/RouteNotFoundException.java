package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Ошибка, возникающая если не найдены подходящие метаданные по url
 */
public class RouteNotFoundException extends N2oException {

    public RouteNotFoundException(String url) {
        super(String.format("Metadata by url '%s' not found!", url));
        setHttpStatus(404);
    }


}
