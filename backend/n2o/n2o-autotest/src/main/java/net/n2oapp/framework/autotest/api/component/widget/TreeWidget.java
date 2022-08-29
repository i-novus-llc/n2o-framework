package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.component.Badge;
import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.Expandable;

public interface TreeWidget extends StandardWidget {
    TreeWidget.TreeItem item(int index);

    void shouldHaveItems(int size);

    interface TreeItem extends Expandable, Component, Badge {
        void shouldHaveItem(String label);
    }
}
