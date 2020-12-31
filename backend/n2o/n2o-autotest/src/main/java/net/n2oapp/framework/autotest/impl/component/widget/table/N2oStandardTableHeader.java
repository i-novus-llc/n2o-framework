package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.table.StandardTableHeader;

/**
 * Стандартный заголовок столбца таблицы для автотестирования
 */
public class N2oStandardTableHeader extends N2oTableHeader implements StandardTableHeader {
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
        if (element().$(".n2o-advanced-table-header-title").exists()) {
            element().$(".n2o-advanced-table-header-title")
                    .shouldHave(Condition.attributeMatching("style", ".*" + style + ".*"));
        } else {
            element().$("div.n2o-advanced-table-header-cel")
                    .shouldHave(Condition.attributeMatching("style", ".*" + style + ".*"));
        }
    }

    @Override
    public void shouldHaveCssClass(String cssClass) {
        element().$("div.n2o-advanced-table-header-cell-content")
                .shouldHave(Condition.attributeMatching("class", ".*" + cssClass + ".*"));
    }
}
