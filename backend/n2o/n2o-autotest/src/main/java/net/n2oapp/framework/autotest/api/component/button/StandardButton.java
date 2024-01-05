package net.n2oapp.framework.autotest.api.component.button;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Tooltip;

import java.time.Duration;

/**
 * Стандартная кнопка для автотестирования
 */
public interface StandardButton extends Button {

    /**
     * Проверка точного соответствия метки (без учета регистра) у кнопки
     * @param label ожидаемое значение метки
     */
    void shouldHaveLabel(String label, Duration... duration);

    /**
     * Проверка описания на соответствие
     * @param text ожидаемый текст описания
     */
    void shouldHaveDescription(String text, Duration... duration);

    /**
     * Проверка класса иконки у кнопки на соответствие ожидаемому значению
     * @param iconName ожидаемый класс иконки
     */
    void shouldHaveIcon(String iconName);

    /**
     * Проверка отсутствия иконки у кнопки
     */
    void shouldNotHaveIcon();

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
