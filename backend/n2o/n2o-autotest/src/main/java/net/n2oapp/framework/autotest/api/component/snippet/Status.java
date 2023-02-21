package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.api.metadata.meta.badge.Position;
import net.n2oapp.framework.autotest.Colors;

/**
 * Компонент отображения статуса для автотестирования
 */
public interface Status extends Snippet {

    /**
     * Проверка соответствия позиции текста
     * @param position ожидаемая позиция текста
     */
    void shouldHaveTextPosition(Position position);


    /**
     * Проверка соответствия текста
     * @param color ожидаемый цвет
     */
    void shouldHaveColor(Colors color);
}
