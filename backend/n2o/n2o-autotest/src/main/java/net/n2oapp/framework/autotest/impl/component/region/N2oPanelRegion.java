package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.PanelRegion;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;

import java.time.Duration;

/**
 * Регион в виде панели для автотестирования
 */
public class N2oPanelRegion extends N2oRegion implements PanelRegion {
    @Override
    public RegionItems content() {
        return N2oSelenide.collection(firstLevelElements(".card-body", "div"), RegionItems.class);
    }

    @Override
    public void shouldHaveTitle(String title, Duration... duration) {
        should(Condition.text(title), header(), duration);
    }

    @Override
    public void shouldNotHaveTitle() {
        header().shouldNot(Condition.exist);
    }

    @Override
    public void shouldHaveFooterTitle(String footer, Duration... duration) {
        should(Condition.text(footer), element().$(".card-footer"), duration);
    }

    @Override
    public void expand() {
        if (collapseToggleBtn().is(collapsedCondition()))
            collapseToggleBtn().click();
    }

    @Override
    public void collapse() {
        if (!collapseToggleBtn().is(collapsedCondition()))
            collapseToggleBtn().click();
    }

    @Override
    public void shouldBeCollapsible() {
        collapseToggleBtn().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeCollapsible() {
        collapseToggleBtn().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeExpanded() {
        collapseToggleBtn().shouldNotHave(collapsedCondition());
    }

    @Override
    public void shouldBeCollapsed() {
        collapseToggleBtn().shouldHave(collapsedCondition());
    }

    @Override
    public void shouldHaveBorderColor(ColorsEnum color) {
        element().shouldHave(Condition.cssClass(color.name("border-")));
    }

    @Override
    public void shouldHaveIcon(String icon) {
        element().$(".card-header .n2o-icon").shouldHave(Condition.cssClass(icon));
    }

    protected SelenideElement collapseToggleBtn() {
        return element().$("button.collapse-toggle");
    }

    private WebElementCondition collapsedCondition() {
        return Condition.cssClass("collapse-toggle--up");
    }

    protected SelenideElement header() {
        return element().$(".card-header");
    }
}
