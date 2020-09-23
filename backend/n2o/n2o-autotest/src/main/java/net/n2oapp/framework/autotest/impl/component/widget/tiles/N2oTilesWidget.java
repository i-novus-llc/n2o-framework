package net.n2oapp.framework.autotest.impl.component.widget.tiles;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.tiles.Tile;
import net.n2oapp.framework.autotest.api.component.widget.tiles.TilesWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oStandardWidget;

public class N2oTilesWidget extends N2oStandardWidget implements TilesWidget {
    @Override
    public Tile tile(int index) {
        return N2oSelenide.component(element().$$(".n2o-tiles__item").get(index), Tile.class);
    }

    @Override
    public void shouldHaveItems(int count) {
        element().$$(".n2o-tiles__item").shouldHaveSize(count);
    }

    @Override
    public Paging paging() {
        return new N2oPaging();
    }

    public class N2oPaging implements Paging {

        @Override
        public void activePageShouldBe(String label) {
            element().$(".n2o-pagination .page-item.active .page-link").shouldHave(Condition.text(label));
        }

        @Override
        public void selectPage(String number) {
            element().$$(".n2o-pagination .page-item .page-link").findBy(Condition.text(number)).click();
        }

        @Override
        public void totalElementsShouldBe(int count) {
            element().$(".n2o-pagination .n2o-pagination-info").scrollTo().should(Condition.matchesText("" + count));
        }
    }
}
