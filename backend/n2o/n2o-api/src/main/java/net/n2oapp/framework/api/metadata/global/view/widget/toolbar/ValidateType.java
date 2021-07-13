package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Тип валидации виджетов страницы
 */
public enum ValidateType implements IdAware {
    PAGE("page"),
    WIDGET("widget"),
    NONE("none"),

    // Deprecated
    TRUE("widget"),
    FALSE("none");

    private String value;

    ValidateType(String value) {
        this.value = value;
    }

    @Override
    public String getId() {
        return this.name().toLowerCase();
    }

    public String getValue() {
        return value;
    }

    @Override
    public void setId(String id) {
    }
}
