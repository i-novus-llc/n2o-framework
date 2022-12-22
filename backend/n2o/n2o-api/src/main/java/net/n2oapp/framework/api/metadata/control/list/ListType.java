package net.n2oapp.framework.api.metadata.control.list;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Тип выбора в выпадающем списке
 */
public enum ListType implements IdAware {
    SINGLE("single"),
    MULTI("multi"),
    CHECKBOXES("checkboxes");

    private final String value;

    ListType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String getId() {
        return this.value;
    }

    @Override
    public void setId(String id) {
        throw new UnsupportedOperationException();
    }
}
