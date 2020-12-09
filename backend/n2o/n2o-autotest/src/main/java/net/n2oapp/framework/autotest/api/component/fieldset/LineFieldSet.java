package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.collection.Fields;

/**
 * Филдсет с горизонтальным делителем для автотестирования
 */
public interface LineFieldSet extends FieldSet {
    Fields fields();

    void shouldBeCollapsible();

    void shouldNotBeCollapsible();

    void expandContent();

    void collapseContent();

    void shouldBeExpanded();

    void shouldBeCollapsed();
}
