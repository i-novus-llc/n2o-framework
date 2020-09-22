package net.n2oapp.framework.autotest.impl.component.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;

public class N2oButtonField extends N2oField implements ButtonField {
    @Override
    public void click() {
        element().$(".btn").shouldBe(Condition.exist).click();
    }
}
