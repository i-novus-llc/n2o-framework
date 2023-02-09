package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;

/**
 * Ячейка таблицы с изображением для автотестирования
 */
public interface ImageCell extends Cell {

    void shouldHaveSrc(String src);

    void shouldHaveImageByUrl(String url);

    void shouldHaveWidth(int width);

    void shouldHaveShape(ShapeType shape);

    void shouldHaveTitle(String title);

    void shouldHaveDescription(String description);

    void shouldHaveTextPosition(TextPosition textPosition);

    default void shouldHaveStatus(ImageStatusElementPlace position, String title) {
        shouldHaveStatus(position, 0, title);
    }

    void shouldHaveStatus(ImageStatusElementPlace position, int index, String title);

    default void shouldHaveStatusWithIcon(ImageStatusElementPlace position, String icon){
        shouldHaveStatusWithIcon(position, 0, icon);
    }

    void shouldHaveStatusWithIcon(ImageStatusElementPlace position, int index, String icon);

}
