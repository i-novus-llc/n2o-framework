package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.autotest.Colors;

/**
 * Компонент отображения статуса для автотестирования
 */
public interface Status extends Snippet {

    /**
     * Проверка того, что текст расположен слева
     */
    void shouldHaveTextOnLeftPosition();

    /**
     * Проверка того, что текст расположен справа
     */
    void shouldHaveTextOnRightPosition();

    /**
     * Проверка соответствия текста
     * @param color ожидаемый цвет
     */
    void shouldHaveColor(Colors color);
}
