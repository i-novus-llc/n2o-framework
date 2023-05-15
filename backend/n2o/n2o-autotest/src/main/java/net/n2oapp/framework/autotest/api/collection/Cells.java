package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;

/**
 * Ячейки таблицы для автотестирования
 */
public interface Cells extends ComponentsCollection {
    /**
     * <p>
     *     Возвращает текстовую ячейку по индексу из списка ячеек в строке
     * </p>
     *
     * <p>For example: {@code
     *    row(0).cell(1);
     * }</p>
     *
     * @param index номер текстовой ячейки
     * @return Компонент текстовая ячейка для автотестирования
     */
    TextCell cell(int index);

    /**
     * <p>
     *     Возвращает ячейку требуемого типа из списка ячеек в строке по индексу
     * </p>
     *
     * <p>For example: {@code
     *    .row(0).cell(0, CheckboxCell.class);
     * }</p>
     *
     * @param index номер ячейки
     * @param componentClass тип возвращаемой ячейки
     * @return Компонент ячейка для автотестирования
     */
    <T extends Cell> T cell(int index, Class<T> componentClass);

    /**
     * <p>
     *     Возвращает ячейку требуемого типа по условию из списка ячеек в строке
     * </p>
     *
     * <p>For example: {@code
     *    .row(0).cell(Condition.visible, CheckboxCell.class);
     * }</p>
     *
     * @param findBy условие поиска
     * @param componentClass возвращаемый тип ячейки
     * @return Компонент ячейка для автотестирования
     */
    <T extends Cell> T cell(Condition findBy, Class<T> componentClass);

    /**
     * Клик по строке с ячейками
     */
    void click();

    /**
     * Проверка возможности клика по строке с ячейками
     */
    void shouldBeClickable();

    /**
     * Проверка недоступности клика по строке с ячейками
     */
    void shouldNotBeClickable();

    /**
     * Наведение мыши на строку с ячейками
     */
    void hover();

    /**
     * Проверка цвета строки по css классу
     * @param color enum с цветовыми кодами
     */
    void shouldHaveColor(Colors color);
}
