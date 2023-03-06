package net.n2oapp.framework.autotest.api.component.widget.table;

import net.n2oapp.framework.autotest.api.component.control.Control;

/**
 * Заголовок фильтруемого столбца таблицы для автотестирования
 */
public interface TableFilterHeader extends TableSimpleHeader {

    /**
     * Возвращает поле ввода фильтрации
     * @param componentClass тип возвращаемого поля ввода
     * @return Компонент ввода для автотестирования
     */
    <T extends Control> T filterControl(Class<T> componentClass);

    /**
     * Открытие фильтра
     */
    void openFilterDropdown();

    /**
     * Клик по кнопке поиска элементов по фильтру
     */
    void clickSearchButton();

    /**
     * Клик по кнопке удаления введенного фильтра
     */
    void clickResetButton();
}
