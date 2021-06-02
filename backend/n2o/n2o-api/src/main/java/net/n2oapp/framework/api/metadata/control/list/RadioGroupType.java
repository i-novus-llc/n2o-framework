package net.n2oapp.framework.api.metadata.control.list;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RadioGroupType {
    defaultType("default"),
    btn("btn"),
    tabs("tabs");

    private final String value;

    RadioGroupType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
