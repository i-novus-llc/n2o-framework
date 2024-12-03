package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.LineRegion;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;

import java.time.Duration;

/**
 * Регион с горизонтальным делителем для автотестирования
 */
public class N2oLineRegion extends N2oRegion implements LineRegion {
    @Override
    public RegionItems content() {
        return content("nested-content");
    }

    @Override
    public RegionItems content(String className) {
        return N2oSelenide.collection(firstLevelElements(".rc-collapse-content-box", "div > ." + className), RegionItems.class);
    }

    @Override
    public void shouldBeCollapsible() {
        header().shouldNotHave(Condition.cssClass("n2o-disabled"));
    }

    @Override
    public void shouldNotBeCollapsible() {
        header().shouldHave(Condition.cssClass("n2o-disabled"));
    }

    @Override
    public void shouldHaveLabel(String title, Duration... duration) {
        should(Condition.text(title), header().$(".n2o-panel-header-text"), duration);
    }

    @Override
    public void expand() {
        if (!item().is(expandedContentCondition()))
            header().click();
    }

    @Override
    public void collapse() {
        if (item().is(expandedContentCondition()))
            header().click();
    }

    @Override
    public void shouldBeExpanded() {
        item().shouldBe(expandedContentCondition());
    }

    @Override
    public void shouldBeCollapsed() {
        item().shouldNotBe(expandedContentCondition());
    }


    protected SelenideElement header() {
        return element().$(".n2o-panel-header");
    }

    protected SelenideElement item() {
        return element().$(".rc-collapse-item");
    }

    private WebElementCondition expandedContentCondition() {
        return Condition.cssClass("rc-collapse-item-active");
    }
}
