package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Ячейка списковых виджетов (table, list) для автотестирования
 */
public class N2oCell extends N2oComponent implements Cell {

    @Override
    public void shouldBeEmpty() {
        element().shouldBe(Condition.empty);
    }

    @Override
    public void shouldBeExpandable() {
        element().$(".n2o-advanced-table-expand .n2o-advanced-table-expand-icon").shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeExpandable() {
        element().$(".n2o-advanced-table-expand .n2o-advanced-table-expand-icon").shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeExpanded() {
        element().$(".n2o-advanced-table-expand .n2o-advanced-table-expand-icon-expanded").shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeExpanded() {
        element().$(".n2o-advanced-table-expand .n2o-advanced-table-expand-icon-expanded").shouldNotBe(Condition.exist);
    }

    @Override
    public void clickExpand() {
        element().$(".n2o-advanced-table-expand .n2o-advanced-table-expand-icon").shouldBe(Condition.exist).click();
    }

    @Override
    public void shouldHaveIcon(String icon) {
        element().$(".n2o-icon").shouldHave(Condition.cssClass(icon));
    }

    @Override
    public void shouldNotHaveIcon() {
        element().$(".n2o-icon").shouldNotBe(Condition.exist);
    }
}
