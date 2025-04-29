package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Сторона боковой панели
 */
public enum SidebarSideEnum implements IdAware {
    LEFT("left"),
    RIGHT("right");

    private final String value;

    SidebarSideEnum(String value) {
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
