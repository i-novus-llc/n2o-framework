package net.n2oapp.framework.api.metadata.global.view.widget.table.column;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Позиция фиксации столбца
 */
public enum ColumnFixedPositionEnum implements IdAware {
    LEFT("left"),
    RIGHT("right");

    private final String value;

    ColumnFixedPositionEnum(String value) {
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
