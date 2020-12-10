package net.n2oapp.framework.autotest.api.component.widget.cards;

import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

/**
 * Списковый виджет Cards для автотестирования
 */
public interface CardsWidget extends StandardWidget {

    Card card(int index);

    void shouldHaveItems(int count);

    Paging paging();
}
