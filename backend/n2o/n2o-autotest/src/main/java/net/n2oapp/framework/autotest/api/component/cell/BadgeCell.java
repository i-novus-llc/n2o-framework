package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.badge.Badge;

/**
 * Ячейка таблицы с текстом для автотестирования
 */
public interface BadgeCell extends Cell, Badge {
    /**
     * Проверка цвета ячейки на соответствие ожидаемому
     * @param color ожидаемый цвет ячейки
     */
    void shouldHaveColor(Colors color);
}
