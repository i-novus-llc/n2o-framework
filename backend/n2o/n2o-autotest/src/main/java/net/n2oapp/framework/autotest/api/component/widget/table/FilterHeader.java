package net.n2oapp.framework.autotest.api.component.widget.table;

import net.n2oapp.framework.autotest.api.component.control.Control;

/**
 * Фильтруемый заголовок таблицы для автотестирования
 */
public interface FilterHeader extends StandardTableHeader {

    <T extends Control> T filterControl(Class<T> componentClass);

    void openFilterDropdown();

    void clickSearchButton();

    void clickResetButton();
}
