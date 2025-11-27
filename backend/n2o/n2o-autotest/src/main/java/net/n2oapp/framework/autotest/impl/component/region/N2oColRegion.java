package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.ColRegion;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;

/**
 * Реализация региона {@code <col>} для автотестирования
 */
public class N2oColRegion extends N2oRegion implements ColRegion {

    @Override
    public RegionItems content() {
        return N2oSelenide.collection(element().$$(":scope > section, :scope > div"), RegionItems.class);
    }

    @Override
    public void shouldHaveSize(int size) {
        element().shouldHave(Condition.attributeMatching("style", String.format(".*--col-span: %d;.*", size)));
    }

    @Override
    public void shouldHaveOffset(int offset) {
        element().shouldHave(Condition.attributeMatching("style", String.format(".*--col-offset: %d;.*", offset)));
    }
}
