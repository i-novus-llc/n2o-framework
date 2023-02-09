package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Компонент пагинации для автотестирования
 */
public interface Paging extends Component {
    void shouldHaveActivePage(String number);

    void selectPage(String number);

    void shouldHavePageNumber(String number);

    void shouldHaveLayout(Layout layout);

    int totalElements();

    void shouldHaveTotalElements(int count);

    void shouldNotHaveTotalElements();

    void shouldNotHavePrev();

    void shouldHavePrev();

    void shouldHavePrevWithLabel(String label);

    void shouldHavePrevWithIcon(String icon);

    void selectPrev();

    void shouldNotHaveNext();

    void shouldHaveNext();

    void shouldHaveNextWithLabel(String label);

    void shouldHaveNextWithIcon(String icon);

    void selectNext();

    void shouldNotHaveFirst();

    void shouldHaveFirst();

    void shouldHaveFirstWithLabel(String label);

    void shouldHaveFirstWithIcon(String icon);

    void selectFirst();

    void shouldNotHaveLast();

    void shouldHaveLast();

    void shouldHaveLastWithLabel(String label);

    void shouldHaveLastWithIcon(String icon);

    void selectLast();


    enum Layout {
        BORDERED("bordered"),
        FLAT("flat"),
        SEPARATED("separated"),
        BORDERED_ROUNDED("bordered-rounded"),
        FLAT_ROUNDED("flat-rounded"),
        SEPARATED_ROUNDED("separated-rounded");

        private final String title;

        Layout(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
