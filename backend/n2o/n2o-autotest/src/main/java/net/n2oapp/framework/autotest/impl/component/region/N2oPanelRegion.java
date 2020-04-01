package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.region.PanelRegion;

public class N2oPanelRegion extends N2oRegion implements PanelRegion {
    @Override
    public Widgets content() {
        return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout"), Widgets.class);
    }

    @Override
    public void toggleCollapse() {
        element().$("button.collapse-toggle").click();
    }


    @Override
    public void shouldBeExpanded() {
        element().$("button.collapse-toggle").shouldNotHave(Condition.cssClass("collapse-toggle--up"));
    }

    @Override
    public void shouldBeCollapsed() {
        element().$("button.collapse-toggle").shouldHave(Condition.cssClass("collapse-toggle--up"));
    }
}
