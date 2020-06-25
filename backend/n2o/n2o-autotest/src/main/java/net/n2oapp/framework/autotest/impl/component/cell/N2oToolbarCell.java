package net.n2oapp.framework.autotest.impl.component.cell;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;

/**
 * Ячейка таблицы с кнопками для автотестирования
 */
public class N2oToolbarCell extends N2oCell implements ToolbarCell {
    @Override
    public Toolbar toolbar() {
        return N2oSelenide.collection(element().$$(".n2o-buttons-cell .btn"), Toolbar.class);
    }
}
