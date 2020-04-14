package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;

/**
 * Ячейка таблицы с Toolbar для автотестирования
 */
public class N2oToolbarCell extends N2oCell implements ToolbarCell {
    @Override
    public void clickMenu(String menuName) {
        element().scrollTo();
        element().$(".n2o-dropdown-control.btn").click();
        element().$$(".n2o-dropdown-menu .btn").findBy(Condition.text(menuName)).shouldBe(Condition.exist).click();
    }

    @Override
    public void itemsShouldBe(int count) {
        element().$$(".n2o-dropdown-menu .btn").shouldHaveSize(count);
    }

    @Override
    public void itemsTextShouldBe(int index, String text) {
        element().$(".n2o-dropdown-control.btn").click();
        element().$$(".n2o-dropdown-menu .btn").get(index).shouldBe(Condition.text(text));
        element().$(".n2o-dropdown-control.btn").click();
    }
}
