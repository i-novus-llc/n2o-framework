package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Филдсет для автотестирования
 */
public interface FieldSet extends Component {
    Fields fields();

    void shouldBeEmpty();
}
