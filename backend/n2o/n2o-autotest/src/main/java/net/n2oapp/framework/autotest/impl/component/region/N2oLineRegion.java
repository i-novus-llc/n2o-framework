package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.region.LineRegion;

/**
 * Регион с горизонтальным делителем для автотестирования
 */
public class N2oLineRegion extends N2oRegion implements LineRegion {
    @Override
    public Widgets content() {
        return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout"), Widgets.class);
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
    public void expandContent() {
        if (!item().is(expandedContentCondition()))
            header().click();
    }

    @Override
    public void collapseContent() {
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


    private SelenideElement header() {
        return element().$(".n2o-panel-header");
    }

    private SelenideElement item() {
        return element().$(".rc-collapse-item");
    }

    private Condition expandedContentCondition() {
        return Condition.cssClass("rc-collapse-item-active");
    }
}
