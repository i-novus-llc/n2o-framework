package net.n2oapp.framework.autotest.impl.component.widget.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.table.TableHeader;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Заголовки столбцов таблицы для автотестирования
 */
public abstract class N2oTableHeader extends N2oComponent implements TableHeader {
    @Override
    public void shouldHaveTitle(String title, Duration... duration) {
        should(Condition.text(title), duration);
    }

    @Override
    public void shouldNotHaveTitle() {
        element().shouldBe(Condition.empty);
    }

    @Override
    public void click() {
        SelenideElement elm = element().$(".checkbox-cell .n2o-checkbox .custom-control-label");
        if (elm.exists())
            elm.click();
        else
            element().$("a").should(Condition.exist).click();
    }

    @Override
    public void shouldNotBeSorted() {
        sortingIcon().shouldNot(Condition.exist);
    }

    @Override
    public void shouldBeSortedByAsc() {
        sortingIcon().should(Condition.exist, Condition.cssClass("fa-sort-amount-asc"));
    }

    @Override
    public void shouldBeSortedByDesc() {
        sortingIcon().should(Condition.exist, Condition.cssClass("fa-sort-amount-desc"));
    }

    @Override
    public void shouldHaveStyle(String style) {
        element().shouldHave(Condition.attributeMatching("style", String.format(".*%s.*", style)));
    }

    @Override
    public void shouldHaveIcon(String icon) {
        icon().shouldHave(Condition.cssClass(icon));
    }

    @Override
    public void shouldNotHaveIcon() {
        icon().shouldNotBe(Condition.exist);
    }

    protected SelenideElement icon() {
        return element().$(".n2o-icon");
    }

    protected SelenideElement sortingIcon() {
        return element().$(".n2o-sorting-icon");
    }
}
