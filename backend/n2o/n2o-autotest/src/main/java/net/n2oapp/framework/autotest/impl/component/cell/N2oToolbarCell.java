package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;

public class N2oToolbarCell extends N2oCell implements ToolbarCell {
    @Override
    public void clickMenu(String menuName) {
        element().$(".n2o-dropdown-control.btn").click();
        element().$$(".n2o-dropdown-menu .btn").findBy(Condition.text(menuName)).shouldBe(Condition.exist).click();
    }
}
