package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Режим перемещения колонки
 */
public enum MoveModeEnum implements IdAware {
    TABLE("table"),
    RIGHT("settings"),
    ALL("all");

    private final String value;

    MoveModeEnum(String value) {
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
