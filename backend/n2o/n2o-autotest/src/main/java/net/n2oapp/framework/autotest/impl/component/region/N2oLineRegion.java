package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.region.LineRegion;

public class N2oLineRegion extends N2oRegion implements LineRegion {
    @Override
    public Widgets content() {
        return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout"), Widgets.class);
    }

    @Override
    public void toggleCollapse() {
        element().$(".rc-collapse-header.n2o-panel-header").click();
    }

    @Override
    public void shouldBeExpanded() {
        element().$(".rc-collapse-item").should(Condition.cssClass("rc-collapse-item-active"));
    }

    @Override
    public void shouldBeCollapsed() {
        element().$(".rc-collapse-content").should(Condition.cssClass("rc-collapse-content-inactive"));
    }
}
