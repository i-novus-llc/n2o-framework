package net.n2oapp.framework.autotest.api.component.header;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Элемент выпадающего списка поиска в шапке для автотестирования
 */
public interface SearchItem extends Component {

    void shouldHaveTitle(String title);

    void shouldHaveLink(String url);

    void shouldHaveDescription(String description);

    void shouldHaveIcon(String icon);

}