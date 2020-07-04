package net.n2oapp.framework.autotest.impl.collection;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.component.fieldset.FieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

/**
 * Филдсеты для автотестирования
 */
public class N2oFieldSets extends N2oComponentsCollection implements FieldSets {

    @Override
    public SimpleFieldSet fieldset(int index) {
        return fieldset(index, SimpleFieldSet.class);
    }

    @Override
    public <T extends FieldSet> T fieldset(int index, Class<T> componentClass) {
        return component(elements().get(index), componentClass);
    }
}
