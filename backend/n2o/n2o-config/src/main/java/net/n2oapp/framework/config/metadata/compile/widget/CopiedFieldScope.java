package net.n2oapp.framework.config.metadata.compile.widget;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Используется для сбора идентификаторов полей, данные которых необходимо копировать при upload=copy
 */
public class CopiedFieldScope {
    private Set<String> copiedFields;

    public void addCopiedFields(String id) {
        if (copiedFields == null) {
            copiedFields = new HashSet<>();
        }
        copiedFields.add(id);
    }

    public Set<String> getCopiedFields() {
        return copiedFields == null ? null : Collections.unmodifiableSet(copiedFields);
    }
}
