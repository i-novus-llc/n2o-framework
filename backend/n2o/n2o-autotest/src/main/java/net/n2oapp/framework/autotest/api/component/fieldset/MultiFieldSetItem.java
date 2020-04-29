package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Элемент филдсета с динамическим числом полей для автотестирования
 */
public interface MultiFieldSetItem extends Component {
    void shouldHaveLabel(String label);

    void removeButtonShouldExists();

    void removeButtonShouldNotExists();

    void clickRemoveButton();

    void copyButtonShouldExists();

    void copyButtonShouldNotExists();

    void clickCopyButton();

    Fields fields();

    FieldSets fieldsets();
}
