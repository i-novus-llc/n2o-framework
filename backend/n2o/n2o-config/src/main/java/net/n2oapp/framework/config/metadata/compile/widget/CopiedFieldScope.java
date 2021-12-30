package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.local.util.StrictMap;

import java.util.*;

/**
 * Используется для сбора идентификаторов полей, данные которых необходимо копировать при upload=copy
 */
public class CopiedFieldScope {
    private Map<String, Set<String>> datasourceCopiedFields = new StrictMap<>();

    public void addCopiedFields(String id, String sourceDatasourceId) {
        if (datasourceCopiedFields == null) {
            datasourceCopiedFields = new HashMap<>();
            datasourceCopiedFields.put(sourceDatasourceId, new HashSet<>());
        }
        if (!datasourceCopiedFields.containsKey(sourceDatasourceId))
            datasourceCopiedFields.put(sourceDatasourceId, new LinkedHashSet<>());
        datasourceCopiedFields.get(sourceDatasourceId).add(id);
    }

    public Set<String> getCopiedFields(String datasource) {
        return Collections.unmodifiableSet(datasourceCopiedFields.get(datasource));
    }
}
