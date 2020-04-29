package net.n2oapp.framework.autotest.impl.component.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.control.Control;
import net.n2oapp.framework.autotest.api.component.field.StandardField;

/**
 * Стандартное поле формы для автотестирования
 */
public class N2oStandardField extends N2oField implements StandardField {

    static final String CSS_SELECTOR = ".n2o-input, .n2o-input-container, .n2o-input-mask, .n2o-date-picker, " +
            ".n2o-radio-container, .n2o-checkbox-group, .n2o-output-text, .n2o-text-editor, " +
            ".n2o-code-editor, .n2o-html, .n2o-rating-stars, .n2o-slider, .n2o-pill-filter";

    @Override
    public <T extends Control> T control(Class<T> componentClass) {
        return N2oSelenide.component(element().$(CSS_SELECTOR), componentClass);
    }

    @Override
    public void shouldBeRequired() {
        element().$(".n2o-field-label-required").shouldHave(Condition.exist);
    }

    @Override
    public void shouldNotBeRequired() {
        //реализовать, когда понадобится
        throw new UnsupportedOperationException();
    }

    @Override
    public void labelShouldHave(Condition condition) {
        element().$("label").shouldHave(condition);
    }

    @Override
    public void messageShouldHave(Condition condition) {
        //реализовать, когда понадобится
        throw new UnsupportedOperationException();
    }

    @Override
    public void shouldHaveDescription(Condition condition) {
        //реализовать, когда понадобится
        throw new UnsupportedOperationException();
    }

    @Override
    public void shouldHaveValidationMessage(Condition condition) {
        element().$(".n2o-validation-message").shouldHave(condition);
    }
}
