package net.n2oapp.framework.autotest.impl.component.widget.tiles;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.tiles.Tile;
import net.n2oapp.framework.autotest.api.component.widget.tiles.TilesWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oPaging;
import net.n2oapp.framework.autotest.impl.component.widget.N2oStandardWidget;

/**
 * Виджет панели для автотестирования
 */
public class N2oTilesWidget extends N2oStandardWidget implements TilesWidget {
    @Override
    public Tile tile(int index) {
        return N2oSelenide.component(tiles().get(index), Tile.class);
    }

    @Override
    public void shouldHaveItems(int count) {
        tiles().shouldHave(CollectionCondition.size(count));
    }

    @Override
    public Paging paging() {
        return new N2oPaging(element());
    }

    protected ElementsCollection tiles() {
        return element().$$(".n2o-tiles__item");
    }
}
