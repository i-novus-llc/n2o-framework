package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
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

    public static class N2oWidgetToolbar extends N2oComponent implements WidgetToolbar {

        public N2oWidgetToolbar(SelenideElement element) {
            setElement(element);
        }

        @Override
        public Toolbar topLeft() {
            return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout-toolbar--left").first().$$(".btn"), Toolbar.class);
        }

        @Override
        public Toolbar topRight() {
            return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout-toolbar--right").first().$$(".btn"), Toolbar.class);
        }

        @Override
        public Toolbar bottomLeft() {
            return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout-toolbar--left").last().$$(".btn"), Toolbar.class);
        }

        @Override
        public Toolbar bottomRight() {
            return N2oSelenide.collection(element().$$(".n2o-standard-widget-layout-toolbar--right").last().$$(".btn"), Toolbar.class);
        }
    }
}
