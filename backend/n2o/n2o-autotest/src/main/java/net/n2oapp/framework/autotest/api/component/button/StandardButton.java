package net.n2oapp.framework.autotest.api.component.button;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Tooltip;

/**
 * Стандартная кнопка для автотестирования
 */
public interface StandardButton extends Button {

    /**
     * Проверка метки у кнопки на соответствие ожидаемому значению
     * @param label ожидаемое значение метки
     */
    void shouldHaveLabel(String label);

    /**
     * Проверка иконки у кнопки на соответствие ожидаемому значению
     * @param iconName ожидаемое имя иконки
     */
    void shouldHaveIcon(String iconName);

    /**
     * Проверка цвета кнопки на соответствие ожидаемому значению
     * @param color ожидаемый цвет кнопки
     */
    void shouldHaveColor(Colors color);

    /**
     * Наведение мыши на кнопку
     */
    void hover();

    /**
     * @return Компонент тултип для автотестирования
     */
    Tooltip tooltip();
}
