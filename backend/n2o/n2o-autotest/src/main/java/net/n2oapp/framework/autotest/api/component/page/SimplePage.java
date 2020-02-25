package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Widgets;

/**
 *  Простая страница для автотестирования
 */
public interface SimplePage extends StandardPage {
    Widgets single();
}
