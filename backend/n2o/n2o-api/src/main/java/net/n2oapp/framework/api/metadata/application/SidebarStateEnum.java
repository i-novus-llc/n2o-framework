package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/**
 * Состояние боковой панели
 * NONE - Скрыта полностью
 * MICRO - Отображается тонкая полоска
 * MINI - Отображаются только иконки
 * MAXI - Широкая боковая панель
 */
@RequiredArgsConstructor
@Getter
public enum SidebarStateEnum implements N2oEnum {
    NONE("none"),
    MICRO("micro"),
    MINI("mini"),
    MAXI("maxi");

    private final String id;
}
