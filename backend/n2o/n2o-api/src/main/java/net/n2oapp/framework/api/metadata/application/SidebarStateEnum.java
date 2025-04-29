package net.n2oapp.framework.api.metadata.application;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Состояние боковой панели
 * NONE - Скрыта полностью
 * MICRO - Отображается тонкая полоска
 * MINI - Отображаются только иконки
 * MAXI - Широкая боковая панель
 */
public enum SidebarStateEnum implements IdAware {
    NONE("none"),
    MICRO("micro"),
    MINI("mini"),
    MAXI("maxi");

    private final String value;

    SidebarStateEnum(String value) {
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
