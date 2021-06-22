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

    void prevShouldHaveLabel(String label);

    void prevShouldHaveIcon(String icon);

    void selectPrev();

    void nextShouldNotExist();

    void nextShouldExist();

    void nextShouldHaveLabel(String label);

    void nextShouldHaveIcon(String icon);

    void selectNext();

    void firstShouldNotExist();

    void firstShouldExist();

    void firstShouldHaveLabel(String label);

    void firstShouldHaveIcon(String icon);

    void selectFirst();

    void lastShouldNotExist();

    void lastShouldExist();

    void lastShouldHaveLabel(String label);

    void lastShouldHaveIcon(String icon);

    void selectLast();
}
