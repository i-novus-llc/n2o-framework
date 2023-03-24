package net.n2oapp.framework.autotest.impl.component.field;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;

public class N2oButtonField extends N2oField implements ButtonField {
    @Override
    public void click() {
        btn().click();
    }

    @Override
    public void shouldBeEnabled() {
        btn().shouldBe(Condition.enabled);
    }

    @Override
    public void shouldBeDisabled() {
        btn().shouldBe(Condition.disabled);
    }

    protected SelenideElement btn() {
        return element().$(".btn");
    }

}
