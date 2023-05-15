package net.n2oapp.framework.autotest.impl.component.fieldset;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;

/**
 * Простой филдсет для автотестирования
 */
public class N2oSimpleFieldSet extends N2oFieldSet implements SimpleFieldSet {
    @Override
    public Fields fields() {
        return N2oSelenide.collection(element().$$(".n2o-form-group"), Fields.class);
    }

    @Override
    public void shouldBeEmpty() {
        element().shouldBe(Condition.empty);
    }

    @Override
    public void shouldHaveLabel(String label) {
        label().shouldHave(Condition.text(label));
    }

    @Override
    public void shouldNotHaveLabel() {
        label().shouldNot(Condition.exist);
    }

    protected SelenideElement label() {
        return element().$(".n2o-fieldset__label");
    }
}
