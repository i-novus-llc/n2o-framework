package net.n2oapp.framework.autotest.api.component.fieldset;

/**
 * Филдсет с динамическим числом полей для автотестирования
 */
public interface MultiFieldSet extends FieldSet {
    void shouldHaveItems(int count);

    MultiFieldSetItem item(int index);

    void addButtonShouldBeExist();

    void addButtonShouldNotBeExist();

    void addButtonShouldHaveLabel(String label);

    void clickAddButton();

    void removeAllButtonShouldBeExist();

    void removeAllButtonShouldNotBeExist();

    void removeAllButtonShouldHaveLabel(String label);

    void clickRemoveAllButton();
}
