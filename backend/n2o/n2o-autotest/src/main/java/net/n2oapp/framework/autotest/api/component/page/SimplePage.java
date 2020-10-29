package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.component.widget.Widget;

/**
 *  Простая страница для автотестирования
 */
public interface SimplePage extends Page {
    <T extends Widget> T widget(Class<T> componentClass);
}
