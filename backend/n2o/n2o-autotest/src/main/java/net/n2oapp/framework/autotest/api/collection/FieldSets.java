package net.n2oapp.framework.autotest.api.collection;

import net.n2oapp.framework.autotest.api.component.fieldset.FieldSet;

/**
 * Филдсеты для автотестирования
 */
public interface FieldSets extends ComponentsCollection {
    FieldSet fieldset(int index);

    <T extends FieldSet> T fieldset(Class<T> componentClass);

    <T extends FieldSet> T fieldset(int index, Class<T> componentClass);
}
