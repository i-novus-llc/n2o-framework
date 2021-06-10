package net.n2oapp.framework.api.metadata.global.view.widget.table;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Представление компонента
 */
public enum Layout implements IdAware {
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
