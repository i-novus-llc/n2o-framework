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

    void totalElementsShouldNotExist();

    void prevShouldNotExist();

    void prevShouldExist();

    void nextShouldNotExist();

    void nextShouldExist();

    void lastShouldNotExist();

    void lastShouldExist();

    void firstShouldNotExist();

    void firstShouldExist();
}
