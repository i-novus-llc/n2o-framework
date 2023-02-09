package net.n2oapp.framework.autotest.api.component.fieldset;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Элемент филдсета с динамическим числом полей для автотестирования
 */
public interface MultiFieldSetItem extends Component {
    void shouldHaveLabel(String label);

    void shouldHaveRemoveButton();

    void shouldNotHaveRemoveButton();

    void clickRemoveButton();

    void shouldHaveCopyButton();

    void shouldNotHaveCopyButton();

    void clickCopyButton();

    Fields fields();

    FieldSets fieldsets();
}
