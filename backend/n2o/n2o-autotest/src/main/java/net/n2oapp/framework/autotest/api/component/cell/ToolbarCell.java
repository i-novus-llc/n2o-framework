package net.n2oapp.framework.autotest.api.component.cell;

import net.n2oapp.framework.autotest.api.collection.Toolbar;

/**
 * Ячейка таблицы с кнопками для автотестирования
 */
public interface ToolbarCell extends Cell {

    /**
     * @return Панель кнопок для автотестирования
     */
    Toolbar toolbar();
}
