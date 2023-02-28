package net.n2oapp.framework.autotest.impl.component.widget.tiles;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.widget.tiles.Tile;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oTile extends N2oComponent implements Tile {
    @Override
    public Cells blocks() {
        return N2oSelenide.collection(element().$$(".n2o-tiles__item > div, .n2o-tiles__item > span"), Cells.class);
    }

    @Override
    public void shouldHaveWidth(int width) {
        element().shouldHave(Condition.attributeMatching("style", ".*width: " + width + "px.*"));
    }

    @Override
    public void shouldHaveHeight(int height) {
        element().shouldHave(Condition.attributeMatching("style", ".*min-height: " + height + "px.*"));
    }
}
