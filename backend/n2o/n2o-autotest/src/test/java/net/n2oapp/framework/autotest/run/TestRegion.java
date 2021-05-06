package net.n2oapp.framework.autotest.run;

import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.region.Region;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import static net.n2oapp.framework.autotest.N2oSelenide.collection;

public class TestRegion extends N2oComponent implements Region {

    public Widgets content() {
        return collection(element().$$(".widget"), Widgets.class);
    }

    @Override
    public void shouldHaveCssClass(String classname) {

    }

    @Override
    public void shouldHaveStyle(String style) {

    }
}
