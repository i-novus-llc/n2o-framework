package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;

/**
 * Компонент вывода изображения для автотестирования
 */
public interface Image extends Snippet {

    /**
     * Проверка заголовка на соответствие 
     * @param text ожидаемый текст заголовка
     */
    void shouldHaveTitle(String text);

    /**
     * Проверка описания на соответствие 
     * @param text ожидаемый текст описания
     */
    void shouldHaveDescription(String text);

    /**
     * Проверка формы изображения на соответствие
     * @param shape ожидаемая форма
     */
    void shouldHaveShape(ShapeType shape);

    /**
     * Проверка ссылки изображения на соответствие
     * @param url ожидаемая ссылка
     */
    void shouldHaveUrl(String url);

    /**
     * Проверка ширины изображения на соответствие
     * @param width ожидаемая ширина
     */
    void shouldHaveWidth(int width);

    /**
     * Проверка позиции текста на соответствие
     * @param position ожидаемая позиция
     */
    void shouldHaveTextPosition(TextPosition position);
}
