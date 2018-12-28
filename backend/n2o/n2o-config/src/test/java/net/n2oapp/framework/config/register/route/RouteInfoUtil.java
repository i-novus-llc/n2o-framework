package net.n2oapp.framework.config.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.register.route.RouteInfo;

public class RouteInfoUtil {

    public static <D extends Compiled> RouteInfo createRouteInfo(String urlPattern, String metadataId, Class<D> compiledClass) {
        return new RouteInfo(urlPattern, new MockCompileContext<>(metadataId, null, compiledClass));
    }
}
