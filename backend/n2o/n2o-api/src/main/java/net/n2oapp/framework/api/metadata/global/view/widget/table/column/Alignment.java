package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Выравнивание контента в ячейках таблицы
 */
public enum Alignment implements IdAware {
    LEFT("left"),
    RIGHT("right"),
    CENTER("center");

    private final String value;

    Alignment(String value) {
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
