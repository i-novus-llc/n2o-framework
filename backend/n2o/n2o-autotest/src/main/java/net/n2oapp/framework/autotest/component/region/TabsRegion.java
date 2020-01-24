package net.n2oapp.framework.autotest.component.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.factory.Widgets;

public interface TabsRegion extends Region {
    TabItem tab(int index);
    TabItem tab(Condition by);
    Widgets activeTab();

    interface TabItem {
        void click();
        void shouldBeActive();
    }
}
