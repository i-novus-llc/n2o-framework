package net.n2oapp.framework.autotest.api.component.fieldset;

/**
 * Филдсет с динамическим числом полей для автотестирования
 */
public interface MultiFieldSet extends FieldSet {
    void shouldHaveItems(int count);

    MultiFieldSetItem item(int index);

    void shouldHaveAddButton();

    void shouldNotHaveAddButton();

    void shouldHaveEnabledAddButton();

    void shouldHaveDisabledAddButton();

    void shouldHaveAddButtonLabel(String label);

    void clickAddButton();

    void shouldHaveRemoveAllButton();

    void shouldNotHaveRemoveAllButton();

    void shouldHaveRemoveAllButtonLabel(String label);

    void clickRemoveAllButton();
}
