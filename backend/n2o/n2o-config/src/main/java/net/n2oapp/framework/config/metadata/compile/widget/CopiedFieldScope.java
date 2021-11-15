package net.n2oapp.framework.config.metadata.compile.widget;

import java.util.*;

/**
 * Используется для сбора идентификаторов полей, данные которых необходимо копировать при upload=copy
 */
public class CopiedFieldScope {
    private Map<String, Set<String>> copiedFields;

    public void addCopiedFields(String id, String datasource) {
        if (copiedFields == null) {
            copiedFields = new HashMap<>();
            copiedFields.put(datasource, new HashSet<>());
        }
        copiedFields.get(datasource).add(id);
    }

    public Set<String> getCopiedFields(String datasource) {
        if (copiedFields == null || copiedFields.get(datasource) == null)
            return null;
        return Collections.unmodifiableSet(copiedFields.get(datasource));
    }
}
