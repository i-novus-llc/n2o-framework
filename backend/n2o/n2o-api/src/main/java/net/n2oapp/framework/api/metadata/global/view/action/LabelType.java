package net.n2oapp.framework.api.metadata.global.view.action;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Тип отображения заголовка (с иконкой или без)
 */
public enum LabelType implements IdAware {
    TEXT("text"),
    ICON("icon"),
    TEXT_AND_ICON("textAndIcon");

    private final String value;

    LabelType(String value) {
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
