package net.n2oapp.framework.config.metadata.compile;

import lombok.Getter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Текущий маршрут метаданной при её сборке
 */
@Getter
public class ParentRouteScope {
    private String url;
    private Map<String, ModelLink> pathMapping = new HashMap<>();
    private Map<String, ModelLink> queryMapping = new LinkedHashMap<>();

    public ParentRouteScope(String url) {
        this.url = url;
    }

    public ParentRouteScope(String url, Map<String, ModelLink> pathMapping, Map<String, ModelLink> queryMapping) {
        this(url);
        if (pathMapping != null) {
            this.pathMapping.putAll(pathMapping);
        }
        if (queryMapping != null) {
            this.queryMapping.putAll(queryMapping);
        }
    }

    public ParentRouteScope(String route, ParentRouteScope parent) {
        this(RouteUtil.normalize(parent.getUrl() + route), parent.getPathMapping(), parent.getQueryMapping());
    }

    public ParentRouteScope(String route,
                            Map<String, ModelLink> additionalPathMapping, Map<String, ModelLink> additionalQueryMapping,
                            ParentRouteScope parent) {
        this(route, parent);
        if (additionalPathMapping != null)
            pathMapping.putAll(additionalPathMapping);
        if (additionalQueryMapping != null)
            queryMapping.putAll(additionalQueryMapping);
    }

    @Override
    public String toString() {
        return queryMapping != null ? RouteUtil.addQueryParams(url, queryMapping) : url;
    }
}
