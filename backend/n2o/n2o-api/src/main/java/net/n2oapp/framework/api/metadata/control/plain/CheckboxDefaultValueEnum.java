package net.n2oapp.framework.api.metadata.control.plain;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

public enum CheckboxDefaultValueEnum implements IdAware {
    NULL("null"),
    FALSE("false");

    private final String value;

    CheckboxDefaultValueEnum(String value) {
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