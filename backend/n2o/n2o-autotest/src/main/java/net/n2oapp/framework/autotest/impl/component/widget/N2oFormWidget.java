package net.n2oapp.framework.autotest.impl.component.widget;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;

/**
 * Виджет - форма для автотестирования
 */
public class N2oFormWidget extends N2oStandardWidget implements FormWidget {
    @Override
    public Fields fields() {
        return N2oSelenide.collection(element().$$(".n2o-fieldset .n2o-form-group,.n2o-snippet,.n2o-alert"), Fields.class);
    }

    @Override
    public FieldSets fieldsets() {
        return N2oSelenide.collection(element().$$(".n2o-fieldset"), FieldSets.class);
    }
}
