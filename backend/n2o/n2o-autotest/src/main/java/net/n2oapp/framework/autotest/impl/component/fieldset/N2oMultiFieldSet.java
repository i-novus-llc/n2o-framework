package net.n2oapp.framework.autotest.impl.component.fieldset;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSetItem;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

/**
 * Филдсет с динамическим числом полей для автотестирования
 */
public class N2oMultiFieldSet extends N2oFieldSet implements MultiFieldSet {
    @Override
    public SelenideElement element() {
        return super.element().$(".n2o-multi-fieldset");
    }

    @Override
    public void shouldHaveLabel(String label) {
        label().shouldHave(Condition.text(label));
    }

    @Override
    public void shouldNotHaveLabel() {
        label().shouldNot(Condition.exist);
    }

    @Override
    public void shouldHaveItems(int count) {
        element().$$(".n2o-multi-fieldset__item").shouldHaveSize(count);
    }

    @Override
    public void shouldBeEmpty() {
        shouldHaveItems(0);
    }

    @Override
    public MultiFieldSetItem item(int index) {
        return component(element().$$(".n2o-multi-fieldset__item").get(index), MultiFieldSetItem.class);
    }

    @Override
    public void addButtonShouldBeExist() {
        addButton().shouldBe(Condition.exist);
    }

    @Override
    public void addButtonShouldNotBeExist() {
        addButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void addButtonShouldBeEnabled() {
        addButton().shouldBe(Condition.enabled);
    }

    @Override
    public void addButtonShouldBeDisabled() {
        addButton().shouldBe(Condition.disabled);
    }

    @Override
    public void addButtonShouldHaveLabel(String label) {
        addButton().shouldHave(Condition.text(label));
    }

    @Override
    public void clickAddButton() {
        addButton().click();
    }

    @Override
    public void removeAllButtonShouldBeExist() {
        removeAllButton().shouldBe(Condition.exist);
    }

    @Override
    public void removeAllButtonShouldNotBeExist() {
        removeAllButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void removeAllButtonShouldHaveLabel(String label) {
        removeAllButton().shouldHave(Condition.text(label));
    }

    @Override
    public void clickRemoveAllButton() {
        removeAllButton().click();
    }

    private SelenideElement label() {
        return element().parent().$(".n2o-fieldset__label");
    }

    private SelenideElement addButton() {
        return element().$(".n2o-multi-fieldset__add.btn");
    }

    private SelenideElement removeAllButton() {
        return element().$(".n2o-multi-fieldset__remove-all.btn");
    }

    @Override
    protected SelenideElement description() {
        return element().parent().$(".n2o-fieldset__description");
    }
}
