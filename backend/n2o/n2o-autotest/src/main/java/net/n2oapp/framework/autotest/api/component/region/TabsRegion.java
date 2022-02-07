package net.n2oapp.framework.autotest.api.component.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Регион в виде вкладок для автотестирования
 */
public interface TabsRegion extends Region {
    TabItem tab(int index);

    TabItem tab(Condition by);

    void shouldHaveSize(int size);

    void shouldHaveMaxHeight(int height);

    void shouldHaveScrollbar();

    void shouldNotHaveScrollbar();

    interface TabItem extends Component {
        RegionItems content();

        void click();

        void shouldHaveName(String text);

        void shouldNotHaveTitle();

        void shouldBeActive();

        void shouldNotBeActive();

        void shouldBeInvalid();

        void shouldBeValid();

        void scrollUp();

        void scrollDown();
    }
}
