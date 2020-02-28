package net.n2oapp.framework.autotest.impl.component.page;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;

/**
 * Простая страница для автотестирования
 */
public class N2oSimplePage extends N2oPage implements SimplePage {
    @Override
    public Widgets single() {
        return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout"), Widgets.class);
    }
}
