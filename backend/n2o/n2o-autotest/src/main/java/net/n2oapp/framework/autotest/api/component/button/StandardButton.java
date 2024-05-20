package net.n2oapp.framework.autotest.api.component.button;

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

}