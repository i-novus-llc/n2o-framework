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

    interface TabItem extends Component {
        RegionItems content();

        void click();

        void shouldHaveText(String text);

        void shouldBeActive();

        void shouldNotBeActive();
    }
}
