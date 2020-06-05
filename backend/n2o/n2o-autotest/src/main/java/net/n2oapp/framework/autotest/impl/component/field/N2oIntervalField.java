package net.n2oapp.framework.autotest.impl.component.field;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.control.Control;
import net.n2oapp.framework.autotest.api.component.control.IntervalField;

public class N2oIntervalField extends N2oField implements IntervalField {

    @Override
    public <T extends Control> T begin(Class<T> control) {
        return N2oSelenide.component(element().$(".n2o-range-field-start").$(N2oStandardField.CSS_SELECTOR), control);
    }

    @Override
    public <T extends Control> T end(Class<T> control) {
        return N2oSelenide.component(element().$(".n2o-range-field-end").$(N2oStandardField.CSS_SELECTOR), control);
    }
}
