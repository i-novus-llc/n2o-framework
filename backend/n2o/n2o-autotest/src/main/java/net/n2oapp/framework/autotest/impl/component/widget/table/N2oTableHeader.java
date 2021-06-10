package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.table.TableHeader;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Заголовки столбцов таблицы для автотестирования
 */
public abstract class N2oTableHeader extends N2oComponent implements TableHeader {
    @Override
    public void shouldHaveTitle(String title) {
        element().shouldHave(Condition.text(title));
    }

    @Override
    public void shouldNotHaveTitle() {
        element().shouldBe(Condition.empty);
    }

    @Override
    public void click() {
        SelenideElement elm = element().$(".n2o-checkbox");
        if (elm.exists())
            elm.click();
        else
            element().$("a").should(Condition.exist).click();
    }

    @Override
    public void shouldNotBeSorted() {
        element().$(".n2o-sorting-icon").shouldNot(Condition.exist);
    }

    @Override
    public void shouldBeSortedByAsc() {
        element().$(".n2o-sorting-icon.fa-sort-amount-asc").should(Condition.exist);
    }

    @Override
    public void shouldBeSortedByDesc() {
        element().$(".n2o-sorting-icon.fa-sort-amount-desc").should(Condition.exist);
    }

    @Override
    public void shouldHaveStyle(String style) {
        SelenideElement elm = element().$(".n2o-advanced-table-header-title");
        if (!elm.exists())
            elm = element().$("div.n2o-advanced-table-header-cel");
        elm.shouldHave(Condition.attributeMatching("style", ".*" + style + ".*"));
    }

    @Override
    public void shouldBeHidden() {
        element().shouldBe(Condition.hidden);
    }

    @Override
    public void shouldBeVisible() {
        element().shouldBe(Condition.visible);
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
