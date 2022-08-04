package net.n2oapp.framework.api.metadata.global.view.action.control;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Тип сценария открытия ссылки
 */
public enum Target {
    self("_self"),
    newWindow("_blank"),
    application("application");

    private String value;

    Target(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
