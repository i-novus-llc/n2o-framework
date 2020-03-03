package net.n2oapp.framework.autotest.impl.component.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.control.Control;
import net.n2oapp.framework.autotest.api.component.field.StandardField;

/**
 *  Стандартное поле формы для автотестирования
 */
public class N2oStandardField extends N2oField implements StandardField {
    @Override
    public <T extends Control> T control(Class<T> componentClass) {
        return N2oSelenide.component(element().$(
                ".n2o-input, .n2o-input-container, .n2o-input-mask, " +
                ".n2o-date-picker, .n2o-radio-container, .n2o-checkbox-group, " +
                ".n2o-output-text"
        ), componentClass);
    }

    @Override
    public void shouldBeRequired() {

    }

    @Override
    public void shouldNotBeRequired() {

    }

    @Override
    public void labelShouldHave(Condition condition) {

    }

    @Override
    public void messageShouldHave(Condition condition) {

    }

    @Override
    public void shouldHaveDescription(Condition condition) {

    }
}
