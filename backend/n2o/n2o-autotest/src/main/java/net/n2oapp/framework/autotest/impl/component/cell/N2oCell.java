package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Ячейка списковых виджетов (table, list) для автотестирования
 */
public class N2oCell extends N2oComponent implements Cell {

    private static final String EXPAND_ICON_SELECTOR = ".n2o-advanced-table-expand .n2o-advanced-table-expand-icon";

    @Override
    public void shouldBeEmpty() {
        element().shouldBe(Condition.empty);
    }

    @Override
    public void shouldBeExpandable() {
        expandIcon().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeExpandable() {
        expandIcon().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeExpanded() {
        element().$(EXPAND_ICON_SELECTOR + "-expanded")
                .shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeExpanded() {
        element().$(EXPAND_ICON_SELECTOR + "-expanded")
                .shouldNotBe(Condition.exist);
    }

    @Override
    public void expand() {
        expandIcon().shouldBe(Condition.exist).click();
    }

    @Override
    public void shouldHaveIcon(String icon) {
        element().$(".n2o-icon")
                .shouldHave(Condition.cssClass(icon));
    }

    @Override
    public void shouldNotHaveIcon() {
        element().$(".n2o-icon")
                .shouldNotBe(Condition.exist);
    }

    protected SelenideElement expandIcon() {
        return element().$(EXPAND_ICON_SELECTOR);
    }
}
