package net.n2oapp.framework.autotest.api.component.field;

import net.n2oapp.framework.autotest.BadgePosition;
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
