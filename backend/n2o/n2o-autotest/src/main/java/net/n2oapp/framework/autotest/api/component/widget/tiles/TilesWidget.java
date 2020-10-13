package net.n2oapp.framework.autotest.api.component.widget.tiles;

import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

/**
 * Виджет панели для автотестирования
 */
public interface TilesWidget extends StandardWidget {
    Tile tile(int index);

    void shouldHaveItems(int count);

    Paging paging();
}
