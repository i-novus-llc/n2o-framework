package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Информация о саб-моделях.
 */
public class SubModelsScope {

    private Map<String, List<SubModelQuery>> subModelScope;

    public void add(SubModelQuery query, String datasource) {
        if (subModelScope == null) {
            subModelScope = new HashMap<>();
        }
        subModelScope.computeIfAbsent(datasource, k -> new ArrayList<>()).add(query);
    }

    public List<SubModelQuery> get(String datasource) {
        if (subModelScope == null)
            return null;
        return subModelScope.get(datasource);
    }
}
