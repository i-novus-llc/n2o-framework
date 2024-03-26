package net.n2oapp.framework.autotest.api.component.field;

import net.n2oapp.framework.autotest.BadgePosition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.button.Button;

/**
 * Модель компонента ButtonField для автотестирования
 */
public interface ButtonField extends Field, Button {

    /**
     * Проверка класса иконки у кнопки на соответствие ожидаемому значению
     * @param iconName ожидаемый класс иконки
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

    /**
     * Проверка соответствия позиции подсказки
     * @param position ожидаемая позиция подсказки
     */
    void tooltipShouldHavePosition(String position);

    /**
     * Проверка соответствия позиции бейджа ожидаемой позиции
     * @param position ожидаемая позиция бейджа
     */
    void badgeShouldHavePosition(BadgePosition position);

}
