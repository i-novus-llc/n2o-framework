package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Alerts;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Стандартный виджет для автотестирования
 */
public class N2oStandardWidget extends N2oWidget implements StandardWidget {

    @Override
    public WidgetToolbar toolbar() {
        return new N2oWidgetToolbar(element());
    }

    @Override
    public Alerts alerts() {
        return N2oSelenide.collection(element().$$(".n2o-alerts .n2o-alert"), Alerts.class);
    }

    public static class N2oWidgetToolbar extends N2oComponent implements WidgetToolbar {

        private static final String TOOLBAR = ".toolbar_placement_%s .btn";

        public N2oWidgetToolbar(SelenideElement element) {
            setElement(element);
        }

        @Override
        public Toolbar topLeft() {
            return N2oSelenide.collection(element().$$(String.format(TOOLBAR, "topLeft")), Toolbar.class);
        }

        @Override
        public Toolbar topRight() {
            return N2oSelenide.collection(element().$$(String.format(TOOLBAR, "topRight")), Toolbar.class);
        }

        @Override
        public Toolbar bottomLeft() {
            return N2oSelenide.collection(element().$$(String.format(TOOLBAR, "bottomLeft")), Toolbar.class);
        }

        @Override
        public Toolbar bottomRight() {
            return N2oSelenide.collection(element().$$(String.format(TOOLBAR, "bottomRight")), Toolbar.class);
        }
    }
}
