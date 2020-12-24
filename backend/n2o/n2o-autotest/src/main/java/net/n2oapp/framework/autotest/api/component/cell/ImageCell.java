package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;

/**
 * Ячейка таблицы с изображением для автотестирования
 */
public interface ImageCell extends Cell {

    void srcShouldBe(String src);

    void imageShouldBe(String url);

    void widthShouldBe(int width);

    void shapeShouldBe(ImageShape shape);

    void shouldHaveTitle(String title);

    void shouldHaveDescription(String description);

    void shouldHaveTextPosition(TextPosition textPosition);

}
