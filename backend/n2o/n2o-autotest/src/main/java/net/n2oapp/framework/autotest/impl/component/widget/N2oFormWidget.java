package net.n2oapp.framework.autotest.impl.component.widget;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.snippet.Snippet;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;

/**
 * Виджет - форма для автотестирования
 */
public class N2oFormWidget extends N2oStandardWidget implements FormWidget {
    @Override
    public Fields fields() {
        return N2oSelenide.collection(element().$$(".n2o-fieldset .n2o-form-group"), Fields.class);
    }

    @Override
    public <T extends Snippet> T snippet(int index, Class<T> componentClass) {
        return N2oSelenide.component(element().$$(".n2o-text-field, .n2o-status-text, .progress").get(index), componentClass);
    }
}
