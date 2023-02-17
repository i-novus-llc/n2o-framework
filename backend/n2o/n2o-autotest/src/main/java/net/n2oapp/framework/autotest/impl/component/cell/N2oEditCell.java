package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.cell.EditCell;
import net.n2oapp.framework.autotest.api.component.control.Control;

public class N2oEditCell extends N2oCell implements EditCell {

    @Override
    public <T extends Control> T control(Class<T> componentClass) {
        return N2oSelenide.component(editableCell(), componentClass);
    }

    @Override
    public void click() {
        element().scrollTo();
        editableCell().click();
    }

    private SelenideElement editableCell() {
        return element().$(".n2o-editable-cell");
    }

}
