package net.n2oapp.framework.autotest.impl.component.page;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.Widget;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

/**
 * Простая страница для автотестирования
 */
public class N2oSimplePage extends N2oPage implements SimplePage {
    @Override
    public <T extends Widget> T widget(Class<T> componentClass) {
        return component(element().$(".n2o-standard-widget-layout"), componentClass);
    }
}
