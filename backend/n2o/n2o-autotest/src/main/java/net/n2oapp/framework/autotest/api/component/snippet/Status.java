package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.autotest.ColorsEnum;

/**
 * Компонент отображения статуса для автотестирования
 */
public interface Status extends Snippet {

    /**
     * Проверка соответствия позиции текста
     * @param position ожидаемая позиция текста
     */
    void shouldHaveTextPosition(PositionEnum position);


    /**
     * Проверка соответствия текста
     * @param color ожидаемый цвет
     */
    void shouldHaveColor(ColorsEnum color);
}
