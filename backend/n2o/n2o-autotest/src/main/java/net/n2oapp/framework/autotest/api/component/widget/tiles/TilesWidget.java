package net.n2oapp.framework.autotest.api.component.widget.tiles;

import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

public interface TilesWidget extends StandardWidget {
    Tile tile(int index);

    void shouldHaveItems(int count);

    Paging paging();

    interface Paging {

        void activePageShouldBe(String label);

        void selectPage(String number);

        void totalElementsShouldBe(int count);
    }
}
