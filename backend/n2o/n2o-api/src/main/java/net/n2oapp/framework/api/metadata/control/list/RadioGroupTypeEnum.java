package net.n2oapp.framework.api.metadata.control.list;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

public enum RadioGroupTypeEnum implements IdAware {
    DEFAULT("default"),
    BTN("btn"),
    TABS("tabs");

    private final String value;

    RadioGroupTypeEnum(String value) {
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