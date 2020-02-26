package net.n2oapp.framework.autotest.api.collection;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;

/**
 * Кнопки хедера для автотестирования
 */
public interface Menu extends ComponentsCollection {
    <T extends MenuItem> T item(int index, Class<T> componentClass);

    <T extends MenuItem> T item(Condition findBy, Class<T> componentClass);
}
