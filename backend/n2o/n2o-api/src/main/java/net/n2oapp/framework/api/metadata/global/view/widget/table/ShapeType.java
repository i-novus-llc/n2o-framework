package net.n2oapp.framework.api.metadata.global.view.widget.table;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Форма square/circle/rounded
 */
public enum ShapeType implements IdAware {
    SQUARE("square"),
    CIRCLE("circle"),
    ROUNDED("rounded");

    private final String value;

    ShapeType(String value) {
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
