package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.LineRegion;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;

import java.time.Duration;

/**
 * Регион с горизонтальным делителем для автотестирования
 */
public class N2oLineRegion extends N2oRegion implements LineRegion {

    public static final String COLLAPSE_PANEL_CONTENT_ACTIVE = ".collapse-panel-content-active";
    public static final String COLLAPSE_PANEL_CONTENT_INACTIVE = ".collapse-panel-content-inactive";

    @Override
    public RegionItems content() {
        return content("nested-content");
    }

    @Override
    public RegionItems content(String className) {
        return N2oSelenide.collection(firstLevelElements(".collapse-panel-content-box", "div > ." + className), RegionItems.class);
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
        if (element().$(COLLAPSE_PANEL_CONTENT_INACTIVE).exists())
            header().click();
    }

    @Override
    public void collapse() {
        if (element().$(COLLAPSE_PANEL_CONTENT_ACTIVE).exists())
            header().click();
    }

    @Override
    public void shouldBeExpanded() {
        element().$(COLLAPSE_PANEL_CONTENT_ACTIVE).shouldBe(Condition.exist);
    }

    @Override
    public void shouldBeCollapsed() {
        element().$(COLLAPSE_PANEL_CONTENT_INACTIVE).shouldBe(Condition.exist);
    }


    protected SelenideElement header() {
        return element().$(".n2o-panel-header");
    }

    protected SelenideElement item() {
        return element().$(".collapse-panel");
    }
}
