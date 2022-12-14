package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Направление сортировки по умолчанию
 */
public enum SortingDirection implements IdAware {
    ASC("asc"),
    DESC("desc");

    private final String value;

    SortingDirection(String value) {
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
