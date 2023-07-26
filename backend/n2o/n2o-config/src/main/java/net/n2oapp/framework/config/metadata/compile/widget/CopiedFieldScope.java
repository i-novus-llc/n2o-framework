package net.n2oapp.framework.config.metadata.compile.widget;

import java.util.*;

/**
 * Используется для сбора идентификаторов полей, данные которых необходимо копировать при upload=copy
 */
public class CopiedFieldScope {
    private Map<String, Set<String>> datasourceCopiedFields = new HashMap<>();

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
        if (!datasourceCopiedFields.containsKey(datasource))
            return Collections.emptySet();
        return Collections.unmodifiableSet(datasourceCopiedFields.get(datasource));
    }
}
