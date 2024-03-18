package net.n2oapp.framework.autotest.impl.component.fieldset;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSetItem;

import java.time.Duration;

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
    public void shouldHaveLabel(String label, Duration... duration) {
        should(Condition.text(label), label(), duration);
    }

    @Override
    public void shouldNotHaveLabel() {
        label().shouldNot(Condition.exist);
    }

    @Override
    public void shouldHaveItems(int count) {
        items().shouldHave(CollectionCondition.size(count));
    }

    @Override
    public void shouldBeEmpty() {
        shouldHaveItems(0);
    }

    @Override
    public MultiFieldSetItem item(int index) {
        return component(items().get(index), MultiFieldSetItem.class);
    }

    @Override
    public void shouldHaveAddButton() {
        addButton().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveAddButton() {
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
    public void addButtonShouldHaveLabel(String label, Duration... duration) {
        should(Condition.text(label), addButton(), duration);
    }

    @Override
    public void clickAddButton() {
        addButton().click();
    }

    @Override
    public void clickAddButton(String label) {
        addButton(label).click();
    }

    @Override
    public void shouldHaveRemoveAllButton() {
        removeAllButton().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveRemoveAllButton() {
        removeAllButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void removeAllButtonShouldHaveLabel(String label, Duration... duration) {
        should(Condition.text(label), removeAllButton(), duration);
    }

    @Override
    public void clickRemoveAllButton() {
        removeAllButton().click();
    }

    protected SelenideElement label() {
        return element().parent().$(".n2o-fieldset__label");
    }

    protected SelenideElement addButton() {
        return element().$(".n2o-multi-fieldset__add.btn");
    }

    protected SelenideElement addButton(String label) {
        return element().$$(".n2o-multi-fieldset__add.btn").find(Condition.text(label));
    }

    protected SelenideElement removeAllButton() {
        return element().$(".n2o-multi-fieldset__remove-all.btn");
    }

    @Override
    protected SelenideElement description() {
        //ToDo нужно ли переопределять этот метод, если он есть у N2oFieldSet
        return element().parent().$(".n2o-fieldset__description");
    }

    protected ElementsCollection items() {
        return element().$$(".n2o-multi-fieldset__item");
    }
}
