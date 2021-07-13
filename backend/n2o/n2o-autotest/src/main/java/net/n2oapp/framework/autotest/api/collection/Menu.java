package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;
import net.n2oapp.framework.autotest.api.component.header.DropdownMenuItem;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;

/**
 * Меню для автотестирования
 */
public interface Menu extends ComponentsCollection {
    AnchorMenuItem anchor(int index);

    AnchorMenuItem anchor(Condition findBy);

    DropdownMenuItem dropdown(int index);

    DropdownMenuItem dropdown(Condition findBy);

    <T extends MenuItem> T item(int index, Class<T> componentClass);

    <T extends MenuItem> T item(Condition findBy, Class<T> componentClass);
}
