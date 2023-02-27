package net.n2oapp.framework.autotest.impl.component.fieldset;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSetItem;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Элемент филдсета с динамическим числом полей для автотестирования
 */
public class N2oMultiFieldSetItem extends N2oComponent implements MultiFieldSetItem {
    @Override
    public void shouldHaveLabel(String label) {
        element().$(".n2o-multi-fieldset__label").shouldHave(Condition.text(label));
    }

    @Override
    public void shouldHaveRemoveButton() {
        removeButton().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveRemoveButton() {
        removeButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void clickRemoveButton() {
        removeButton().click();
    }

    @Override
    public void shouldHaveCopyButton() {
        copyButton().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveCopyButton() {
        copyButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void clickCopyButton() {
        copyButton().click();
    }

    @Override
    public Fields fields() {
        return N2oSelenide.collection(element().$$(".n2o-fieldset .n2o-form-group"), Fields.class);
    }

    @Override
    public FieldSets fieldsets() {
        return N2oSelenide.collection(element().$$(".n2o-fieldset"), FieldSets.class);
    }

    protected SelenideElement removeButton() {
        return element().$(".n2o-multi-fieldset__remove.btn");
    }

    protected SelenideElement copyButton() {
        return element().$(".n2o-multi-fieldset__copy.btn");
    }
}
