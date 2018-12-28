package net.n2oapp.framework.config.metadata.compile.fieldset;

import java.util.HashSet;

/**
 * Информация о видимости филдсетов
 */
public class FieldSetVisibilityScope extends HashSet<String> {
    public FieldSetVisibilityScope(FieldSetVisibilityScope parent) {
        if (parent != null)
            this.addAll(parent);
    }
}
