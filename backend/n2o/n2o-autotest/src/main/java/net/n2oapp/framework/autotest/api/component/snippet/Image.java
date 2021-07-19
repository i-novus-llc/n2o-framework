package net.n2oapp.framework.autotest.api.component.snippet;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;

/**
 * Компонент вывода изображения для автотестирования
 */
public interface Image extends Snippet {

    void shouldHaveTitle(String text);

    void shouldHaveDescription(String text);

    void shouldHaveShape(ImageShape shape);

    void shouldHaveUrl(String url);

    void shouldHaveWidth(int width);

    void shouldHaveTextPosition(TextPosition position);
}
