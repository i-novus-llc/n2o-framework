package net.n2oapp.framework.autotest.api.component.button;

import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.badge.Badge;

/**
 * Кнопка для автотестирования
 */
public interface Button extends Badge {
    /**
     * Проверка на не доступность клика по кнопке
     */
    void shouldBeDisabled();

    /**
     * Проверка на доступность клика по кнопке
     */
    void shouldBeEnabled();

    /**
     * Клик по кнопке
     */
    void click();

    /**
     * Наведение мыши на кнопку
     */
    void hover();

    /**
     * @return Компонент тултип для автотестирования
     */
    Tooltip tooltip();

    /**
     * Проверка цвета кнопки на соответствие ожидаемому значению
     * @param color ожидаемый цвет кнопки
     */
    void shouldHaveColor(ColorsEnum color);
}
