package net.n2oapp.framework.autotest.impl.component.widget.tiles;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.widget.tiles.Tile;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oTile extends N2oComponent implements Tile {
    @Override
    public Cells cells() {
        return N2oSelenide.collection(element().$$(".n2o-tiles__item > div, .n2o-tiles__item > span"), Cells.class);
    }
}
