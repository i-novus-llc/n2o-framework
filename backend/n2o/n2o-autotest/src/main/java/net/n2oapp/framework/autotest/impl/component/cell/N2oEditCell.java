package net.n2oapp.framework.autotest.impl.component.cell;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.cell.EditCell;
import net.n2oapp.framework.autotest.api.component.control.Control;

public class N2oEditCell extends N2oCell implements EditCell {

    @Override
    public <T extends Control> T control(Class<T> componentClass) {
        return N2oSelenide.component(element().$(".n2o-editable-cell"), componentClass);
    }

    @Override
    public void click() {
        element().scrollTo();
        element().$(".n2o-editable-cell").click();
    }

}
