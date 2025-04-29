package net.n2oapp.framework.api.metadata.meta.badge;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Позиция left/right
 */
public enum PositionEnum implements IdAware {
    LEFT("left"),
    RIGHT("right");

    private final String value;

    PositionEnum(String value) {
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
