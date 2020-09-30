package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;

/**
 * Ячейка таблицы с изображением для автотестирования
 */
public interface ImageCell extends Cell {

    void srcShouldBe(String src);

    void imageShouldBe(String url);

    void widthShouldBe(int width);

    void shapeShouldBe(ImageShape shape);
}
