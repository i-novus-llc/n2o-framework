package net.n2oapp.framework.autotest.impl.component.fieldset;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;

/**
 * Филдсет с динамическим числом полей для автотестирования
 */
public class N2oMultiFieldSet extends N2oFieldSet implements MultiFieldSet {
    @Override
    public SelenideElement element() {
        return super.element().$(".n2o-multi-fieldset");
    }

    ///TODO ????
    @Override
    public Fields fields() {
        return N2oSelenide.collection(element().$$(".n2o-multi-fieldset .n2o-form-group"), Fields.class);
    }


    @Override
    public void shouldHaveItems(int count) {
        element().$$(".n2o-multi-fieldset__item").shouldHaveSize(count);
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
    public void addButtonShouldHaveLabel(String label) {
        addButton().shouldHave(Condition.text(label));
    }

    @Override
    public void clickAddButton() {
        addButton().click();
    }

    private SelenideElement addButton() {
        return element().$(".n2o-multi-fieldset__add.btn");
    }
}
