package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageStatusElement;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;

/**
 * Ячейка таблицы с изображением для автотестирования
 */
public interface ImageCell extends Cell {

    void srcShouldBe(String src);

    void imageShouldBe(String url);

    void widthShouldBe(int width);

    void shapeShouldBe(ShapeType shape);

    void shouldHaveTitle(String title);

    void shouldHaveDescription(String description);

    void shouldHaveTextPosition(TextPosition textPosition);

    default void shouldHaveStatus(N2oImageStatusElement.Place position, String title) {
        shouldHaveStatus(position, 0, title);
    }

    void shouldHaveStatus(N2oImageStatusElement.Place position, int index, String title);

    default void statusShouldHaveIcon(N2oImageStatusElement.Place position, String icon){
        statusShouldHaveIcon(position, 0, icon);
    }

    void statusShouldHaveIcon(N2oImageStatusElement.Place position, int index, String icon);

}
