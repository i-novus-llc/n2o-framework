package net.n2oapp.framework.autotest.api.component.widget.table;

import net.n2oapp.framework.autotest.api.component.control.Control;

/**
 * Заголовок фильтруемого столбца таблицы для автотестирования
 */
public interface TableFilterHeader extends TableSimpleHeader {

    <T extends Control> T filterControl(Class<T> componentClass);

    void openFilterDropdown();

    void clickSearchButton();

    void clickResetButton();
}
