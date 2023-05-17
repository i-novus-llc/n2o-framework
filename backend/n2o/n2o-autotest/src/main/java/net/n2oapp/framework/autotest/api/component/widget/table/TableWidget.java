package net.n2oapp.framework.autotest.api.component.widget.table;

import net.n2oapp.framework.api.metadata.global.view.widget.table.column.SortingDirection;
import net.n2oapp.framework.autotest.api.collection.*;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

import java.util.List;

/**
 * Виджет таблица для автотестирования
 */
public interface TableWidget extends StandardWidget {

    /**
     * @return Компонент колонки таблицы для автотестирования
     */
    Columns columns();

    /**
     * @return Компонент фильтры таблицы для автотестирования
     */
    Filters filters();

    /**
     * @return Компонент пагинации для автотестирования
     */
    Paging paging();

    /**
     * Компонент колонки таблицы для автотестирования
     */
    interface Columns {

        /**
         * @return Заголовки столбцов таблицы для автотестирования
         */
        TableHeaders headers();

        /**
         * @return Компонент строки для автотестирования
         */
        Rows rows();
    }

    /**
     * Компонент фильтры таблицы для автотестирования
     */
    interface Filters {

        /**
         * Возвращает панель кнопок у фильтров
         * @return Компонент панель кнопок
         */
        Toolbar toolbar();

        /**
         * Возвращает поля для задания фильтров таблицы
         * @return Поля формы для автотестирования
         */
        Fields fields();

        /**
         * Возвращает филдсеты с полями для задания фильтров таблицы
         * @return Филдсеты для автотестирования
         */
        FieldSets fieldsets();

        /**
         * Проверка видимости фильтров
         */
        void shouldBeVisible();

        /**
         * Проверка скрытости фильтров
         */
        void shouldBeHidden();

        /**
         * Функция не поддерживаема, следует использовать shouldBeHidden()
         */
        @Deprecated
        void shouldBeInvisible();
    }

    interface Rows {
        /**
         * Возвращает ячейки внутри строки
         * @param index номер строки
         * @return Ячейки таблицы для автотестирования
         */
        Cells row(int index);

        /**
         * Проверка количества строк
         * @param size ожидаемое количество
         */
        void shouldHaveSize(int size);

        /**
         * Проверка отсутствия строк
         */
        void shouldNotHaveRows();

        /**
         * Проверка того, что строка выбрана
         * @param row номер строки
         */
        void shouldBeSelected(int row);

        /**
         * Проверка отсутствия выбранных строк
         */
        void shouldNotHaveSelectedRows();

        /**
         * Проверка того, что колонка имеет соответствующий текст.
         * @param index номер проверяемой колонки
         * @param text ожидаемый текст
         */
        void columnShouldHaveTexts(int index, List<String> text);

        /**
         * Проверка того, что у колонки нет текста.
         * @param index номер проверяемой колонки
         */
        void columnShouldBeEmpty(int index);

        /**
         * Возвращает список текста из колонки
         * @param index номер необходимой колонки
         * @return Список строк
         */
        List<String> columnTexts(int index);

        /**
         * Проверка, что колонка отсортирована
         * @param columnIndex номер колонки
         * @param direction направление отсортированности
         */
        void columnShouldBeSortedBy(int columnIndex, SortingDirection direction);
    }
}
