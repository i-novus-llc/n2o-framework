package net.n2oapp.framework.autotest.api.component.widget;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.snippet.Snippet;

/**
 * Виджет - форма для автотестирования
 */
public interface FormWidget extends StandardWidget {
    Fields fields();

    FieldSets fieldsets();

    <T extends Snippet> T snippet(Class<T> componentClass);
}
