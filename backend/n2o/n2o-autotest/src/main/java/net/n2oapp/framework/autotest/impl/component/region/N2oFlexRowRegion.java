package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.FlexRowRegion;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;

/**
 * Реализация региона {@code <flex-row>} для автотестирования
 */
public class N2oFlexRowRegion extends N2oRegion implements FlexRowRegion {

    @Override
    public RegionItems content() {
        return N2oSelenide.collection(firstLevelElements(".layout-row", "div"), RegionItems.class);
    }

    @Override
    public void shouldHaveAlignment(String alignment) {
        element().shouldHave(Condition.attributeMatching("data-align-items", alignment));
    }

    @Override
    public void shouldBeWrapped() {
        element().shouldNotHave(Condition.cssClass("nowrap"));
    }

    @Override
    public void shouldNotBeWrapped() {
        element().shouldHave(Condition.cssClass("nowrap"));
    }

    @Override
    public void shouldHaveJustify(String justify) {
        element().shouldHave(Condition.attributeMatching("data-justify-content", justify));
    }
}
