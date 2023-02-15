package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlace;
import net.n2oapp.framework.api.metadata.meta.control.TextPosition;

/**
 * Ячейка таблицы с изображением для автотестирования
 */
public interface ImageCell extends Cell {

    /**
     * Проверка того, что ячейка содержит ожидаемый путь к источнику изображения
     * @param src ожидаемый путь к источнику изображения
     */
    void shouldHaveSrc(String src);

    /**
     * Проверка ширины ячейки на соответствие ожидаемому значению
     * @param width ожидаемая ширина ячейки
     */
    void shouldHaveWidth(int width);

    /**
     * Проверка формы ячейки на соответствие ожидаемому значению
     * @param shape ожидаемая форма ячейки
     */
    void shouldHaveShape(ShapeType shape);

    /**
     * Проверка заголовка ячейки на соответствие ожидаемому значению
     * @param title ожидаемый заголовок ячейки
     */
    void shouldHaveTitle(String title);

    /**
     * Проверка описания ячейки на соответствие ожидаемому значению
     * @param description ожидаемое описание ячейки
     */
    void shouldHaveDescription(String description);

    /**
     * Проверка позиции текста ячейки на соответствие ожидаемому значению
     * @param textPosition ожидаемая позиция текста в ячейке
     */
    void shouldHaveTextPosition(TextPosition textPosition);

    /**
     * Проверка заголовка статуса на соответствие ожидаемому значению
     * @param position позиция заголовка статуса
     * @param index номер заголовка статуса
     * @param title ожидаемое значение заголовка статуса
     */
    void shouldHaveStatus(ImageStatusElementPlace position, int index, String title);

    /**
     * Проверка иконки статуса на соответствие ожидаемому значению
     * @param position позиция иконки статуса
     * @param index номер иконки статуса
     * @param icon ожидаемое значение иконки статуса
     */
    void shouldHaveStatusWithIcon(ImageStatusElementPlace position, int index, String icon);

}
