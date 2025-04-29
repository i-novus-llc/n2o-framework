package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Тип доступности кнопки при пустой модели
 */
public enum DisableOnEmptyModelTypeEnum implements IdAware {
    AUTO("auto"),
    TRUE("true"),
    FALSE("false");

    private final String value;

    DisableOnEmptyModelTypeEnum(String value) {
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
