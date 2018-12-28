package net.n2oapp.framework.api.metadata.control.list;

import net.n2oapp.framework.api.metadata.global.aware.IdAware;

/**
 * @author iryabov
 * @since 24.08.2015
 */
public enum GroupClassifierMode implements IdAware {
    SINGLE("single"),
    MULTI("multi"),
    MULTI_CHECKBOX("multi-checkbox");

    public String value;

    GroupClassifierMode(String value) {
        this.value = value;
    }

    @Override
    public String getId() {
        return value;
    }
}
