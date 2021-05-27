package net.n2oapp.framework.api.metadata.global.view.widget.table;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Представление компонента
 */
public enum Layout {
    bordered("bordered"),
    flat("flat"),
    separated("separated"),
    borderedRounded("bordered-rounded"),
    flatRounded("flat-rounded"),
    separatedRounded("separated-rounded");

    private final String value;

    Layout(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
