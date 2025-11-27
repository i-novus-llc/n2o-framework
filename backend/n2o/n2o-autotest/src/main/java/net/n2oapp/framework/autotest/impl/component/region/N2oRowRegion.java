package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.RowRegion;

/**
 * Реализация региона {@code <row>} для автотестирования
 */
public class N2oRowRegion extends N2oRegion implements RowRegion {

    @Override
    public RegionItems content() {
        return N2oSelenide.collection(firstLevelElements(".layout-row", "div"), RegionItems.class);
    }

    @Override
    public void shouldHaveAlignment(String alignment) {
        element().shouldHave(Condition.cssClass(String.format("layout-row--align-%s", alignment)));
    }

    @Override
    public void shouldHaveColumns(String columns) {
        element().shouldHave(Condition.attributeMatching("style", String.format(".*--columns: %s;.*", columns)));
    }

    @Override
    public void shouldBeWrapped() {
        element().shouldHave(Condition.cssClass("layout-row--wrap"));
    }

    @Override
    public void shouldNotBeWrapped() {
        element().shouldHave(Condition.cssClass("layout-row--nowrap"));
    }

    @Override
    public void shouldHaveJustify(String justify) {
        element().shouldHave(Condition.cssClass(String.format("layout-row--justify-%s", justify)));
    }
}
