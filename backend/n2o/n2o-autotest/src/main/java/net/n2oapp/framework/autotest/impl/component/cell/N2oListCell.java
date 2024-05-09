package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.ListCell;

/**
 * Ячейка таблицы со списком для автотестирования
 */
public class N2oListCell extends N2oCell implements ListCell {
    @Override
    public void shouldHaveSize(int size) {
        element().$$(".badge").shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void shouldHaveText(int index, String val) {
        element().$$(".badge").get(index).shouldHave(Condition.text(val));
    }

    @Override
    public void shouldHaveCollapseExpand(boolean visible) {
        if (visible) {
            element().$(".collapsed-cell-control").shouldBe(Condition.exist);
        } else {
            element().$(".collapsed-cell-control").shouldNotBe(Condition.exist);
        }
    }

    @Override
    public void clickCollapseExpand() {
        element().$(".collapsed-cell-control").click();
    }
}
