package net.n2oapp.framework.autotest.api.component;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.snippet.Snippet;

/**
 * Компонент маркдаун-разметки для автотестирования
 */
public interface Markdown extends Snippet {

    /**
     * Возвращает маркдаун-кнопку по метке
     * @param label метка для поиска
     * @return Стандартная кнопка для автотестирования
     */
    StandardButton button(String label);

    /**
     * Проверка наличия элемента по css селектору
     * @param cssSelector css селектр
     */
    void shouldHaveElement(String cssSelector);
}
