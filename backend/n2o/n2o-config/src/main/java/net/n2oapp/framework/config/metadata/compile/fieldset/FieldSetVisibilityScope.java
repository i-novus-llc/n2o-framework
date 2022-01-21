package net.n2oapp.framework.config.metadata.compile.fieldset;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Информация о видимости филдсетов
 */
public class FieldSetVisibilityScope implements Serializable {
    private Set<String> conditions = new LinkedHashSet<>();

    public FieldSetVisibilityScope(FieldSetVisibilityScope parent) {
        if (parent != null)
            conditions.addAll(parent.getConditions());
    }

    public void add(String condition) {
        conditions.add(condition);
    }

    public Set<String> getConditions() {
        return Collections.unmodifiableSet(conditions);
    }
}
