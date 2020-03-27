package net.n2oapp.framework.autotest.api.component.fieldset;

/**
 * Филдсет с динамическим числом полей для автотестирования
 */
public interface MultiFieldSet extends FieldSet {
    void shouldHaveItems(int count);

    void addButtonShouldBeExist();

    void addButtonShouldNotBeExist();

    void addButtonShouldHaveLabel(String label);

    void clickAddButton();
}
