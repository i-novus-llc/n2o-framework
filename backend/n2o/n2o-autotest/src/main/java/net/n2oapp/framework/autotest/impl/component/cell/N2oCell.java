package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Ячейка списковых виджетов (table, list) для автотестирования
 */
public class N2oCell extends N2oComponent implements Cell {

    private static final String EXPAND_ICON_SELECTOR = ".n2o-advanced-table-expand .n2o-advanced-table-expand-icon";

    @Override
    public void shouldBeEmpty(Duration... duration) {
        should(Condition.empty, duration);
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
        expandIcon().shouldHave(Condition.cssClass("fa-angle-down"));
    }

    @Override
    public void shouldBeCollapsed() {
        expandIcon().shouldHave(Condition.cssClass("fa-angle-right"));
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

    @Override
    public void shouldHaveAlignment(String alignment) {
        element().shouldHave(Condition.attributeMatching("align", String.format(".*%s.*", alignment)));
    }
}
