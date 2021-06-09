package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.Dropdown;

/**
 * Филдсет с горизонтальным делителем для автотестирования
 */
public interface LineFieldSet extends FieldSet, Dropdown {
    Fields fields();

    void shouldBeCollapsible();

    void shouldNotBeCollapsible();
}
