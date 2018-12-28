package net.n2oapp.framework.config.metadata.compile;

import lombok.Getter;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.Map;

/**
 * Текущий маршрут метаданной при её сборке
 */
@Getter
public class ParentRoteScope {
    private String url;
    private Map<String, ModelLink> pathMapping = new StrictMap<>();

    public ParentRoteScope(String url) {
        this.url = url;
    }

    public ParentRoteScope(String url, Map<String, ModelLink> pathMapping) {
        this(url);
        if (pathMapping != null) {
            for (Map.Entry<String, ModelLink> entry : pathMapping.entrySet()) {
                addParentPathMapping(entry.getKey(), entry.getValue());
            }
        }
    }

    public ParentRoteScope(String route, ParentRoteScope parent) {
        this(RouteUtil.normalize(parent.getUrl() + route));
        if (parent.getPathMapping() != null)
            this.addParentPathMapping(parent.getPathMapping());
    }

    private void addParentPathMapping(String param, ModelLink bindLink) {
        pathMapping.put(param, bindLink);
    }

    private void addParentPathMapping(Map<String, ModelLink> pathMapping) {
        this.pathMapping.putAll(pathMapping);
    }

    public String getCompiledId() {
        return RouteUtil.convertPathToId(url);
    }

    @Override
    public String toString() {
        return url;
    }
}
