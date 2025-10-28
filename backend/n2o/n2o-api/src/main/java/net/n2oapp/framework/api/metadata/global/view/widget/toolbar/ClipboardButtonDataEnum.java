package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Тип данных для кнопки {@code <clipboard-button>}
 */
public enum ClipboardButtonDataEnum implements IdAware {
    TEXT("text"),
    HTML("html");

    private final String value;

    ClipboardButtonDataEnum(String value) {
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