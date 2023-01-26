package net.n2oapp.framework.autotest.impl.component.widget.cards;

import com.codeborne.selenide.CollectionCondition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.cards.Card;
import net.n2oapp.framework.autotest.api.component.widget.cards.CardsWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oPaging;
import net.n2oapp.framework.autotest.impl.component.widget.N2oStandardWidget;

/**
 * Списковый виджет Cards для автотестирования
 */
public class N2oCardsWidget extends N2oStandardWidget implements CardsWidget {
    @Override
    public Card card(int index) {
        return N2oSelenide.component(element().$$(".n2o-cards").get(index), Card.class);
    }

    @Override
    public void shouldHaveItems(int count) {
        element().$$(".n2o-cards").shouldHave(CollectionCondition.size(count));
    }

    @Override
    public Paging paging() {
        return new N2oPaging(element());
    }
}
