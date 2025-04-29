package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElementPlaceEnum;
import net.n2oapp.framework.api.metadata.meta.control.TextPositionEnum;

import java.time.Duration;

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
    void shouldHaveShape(ShapeTypeEnum shape);

    /**
     * Проверка заголовка ячейки на точное соответствие (без учета регистра) ожидаемому значению
     * @param title ожидаемый заголовок ячейки
     */
    void shouldHaveTitle(String title, Duration... duration);

    /**
     * Проверка описания ячейки на точное соответствие (без учета регистра) ожидаемому значению
     * @param description ожидаемое описание ячейки
     */
    void shouldHaveDescription(String description, Duration... duration);

    /**
     * Проверка позиции текста ячейки на соответствие ожидаемому значению
     * @param textPosition ожидаемая позиция текста в ячейке
     */
    void shouldHaveTextPosition(TextPositionEnum textPosition);

    /**
     * Проверка заголовка статуса на соответствие ожидаемому значению
     * @param position позиция заголовка статуса
     * @param index номер заголовка статуса
     * @param title ожидаемое значение заголовка статуса
     */
    void shouldHaveStatus(ImageStatusElementPlaceEnum position, int index, String title, Duration... duration);

    /**
     * Проверка иконки статуса на соответствие ожидаемому значению
     * @param position позиция иконки статуса
     * @param index номер иконки статуса
     * @param icon ожидаемое значение иконки статуса
     */
    void statusShouldHaveIcon(ImageStatusElementPlaceEnum position, int index, String icon);

}
