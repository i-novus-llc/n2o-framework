package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Тип валидации виджетов страницы
 */
public enum ValidateType implements IdAware {
    PAGE,
    WIDGET,
    NONE;

    @Override
    public String getId() {
        return this.name().toLowerCase();
    }

    @Override
    public void setId(String id) {
    }
}
