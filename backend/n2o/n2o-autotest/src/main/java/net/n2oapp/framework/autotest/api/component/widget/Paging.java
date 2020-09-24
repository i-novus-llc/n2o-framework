package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент пагинации для автотестирования
 */
public interface Paging extends Component {
    void activePageShouldBe(String number);

    void selectPage(String number);

    void pagingShouldHave(String number);

    int totalElements();

    void totalElementsShouldBe(int count);
}
