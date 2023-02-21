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
    public void shouldNotBeExpandable() {
        element().$(".collapsed-cell-control").shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeExpandable() {
        element().$(".collapsed-cell-control").shouldBe(Condition.exist);
    }

    @Override
    public void expand() {
        element().$(".collapsed-cell-control").click();
    }
}
