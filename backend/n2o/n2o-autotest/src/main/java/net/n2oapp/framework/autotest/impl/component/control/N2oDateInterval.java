package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;

public class N2oDateInterval extends N2oControl implements DateInterval {

    @Override
    public void setBeginValue(String value) {
        element().$(".n2o-date-input-first input").setValue(value);
    }

    @Override
    public void setEndValue(String value) {
        element().$(".n2o-date-input-last input").setValue(value);
    }

    @Override
    public void beginShouldHaveValue(String value) {
        element().$(".n2o-date-input-first input").shouldHave(Condition.value(value));
    }

    @Override
    public void endShouldHaveValue(String value) {
        element().$(".n2o-date-input-last input").shouldHave(Condition.value(value));
    }
}
