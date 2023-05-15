package net.n2oapp.framework.autotest.api.component.widget.list;

import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

public interface ListWidget extends StandardWidget {

    /**
     * Возвращает содержимое виджета по номеру
     * @param index номер требуемого содержимого
     * @return Содержимое строк списка для автотестирования
     */
    Content content(int index);

    /**
     * Проверка количества строк
     * @param size ожидаемое количество
     */
    void shouldHaveSize(int size);

    /**
     * @return Компонент пагинации для автотестирования
     */
    Paging paging();

    /**
     * Содержимое строк списка для автотестирования
     */
    interface Content {

        /**
         * Клик по строке
         */
        void click();

        /**
         * Возвращает верхнюю левую ячейку строки
         * @param clazz тип возвращаемой ячейке
         * @return Стандатная ячейка для автотестирования
         */
        <T extends Cell> T leftTop(Class<T> clazz);

        /**
         * Возвращает нижнюю левую ячейку строки
         * @param clazz тип возвращаемой ячейке
         * @return Стандатная ячейка для автотестирования
         */
        <T extends Cell> T leftBottom(Class<T> clazz);

        /**
         * Возвращает шапку строки
         * @param clazz тип возвращаемой ячейки
         * @return Стандатная ячейка для автотестирования
         */
        <T extends Cell> T header(Class<T> clazz);

        /**
         * Возвращает ячейку с телом строки
         * @param clazz тип возвращаемой ячейки
         * @return Стандатная ячейка для автотестирования
         */
        <T extends Cell> T body(Class<T> clazz);

        /**
         * Возвращает подшапку строки
         * @param clazz тип возвращаемой ячейки
         * @return Стандатная ячейка для автотестирования
         */
        <T extends Cell> T subHeader(Class<T> clazz);

        /**
         * Возвращает верхнюю правую ячейку строки
         * @param clazz тип возвращаемой ячейке
         * @return Стандатная ячейка для автотестирования
         */
        <T extends Cell> T rightTop(Class<T> clazz);

        /**
         * Возвращает нижнюю правую ячейку строки
         * @param clazz тип возвращаемой ячейке
         * @return Стандатная ячейка для автотестирования
         */
        <T extends Cell> T rightBottom(Class<T> clazz);

        /**
         * Возвращает ячейку с кнопками
         * @param clazz тип возвращаемой ячейке
         * @return Стандатная ячейка для автотестирования
         */
        <T extends Cell> T extra(Class<T> clazz);
    }
}
