package net.n2oapp.framework.autotest.impl.component.field;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.control.Control;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.impl.collection.N2oToolbar;

/**
 * Стандартное поле формы для автотестирования
 */
public class N2oStandardField extends N2oField implements StandardField {

    static final String CSS_SELECTOR = ".form-control, .n2o-input, .n2o-date-picker, .n2o-radio-group, " +
            ".n2o-checkbox-group, .n2o-file-uploader-control, .n2o-image-uploader-control, .n2o-code-editor, .n2o-html, .n2o-output-text, " +
            ".n2o-output-list, .n2o-pill-filter, .n2o-rating-stars, .n2o-slider, .n2o-text-editor, .progress, .n2o-number-picker";

    @Override
    public <T extends Control> T control(Class<T> componentClass) {
        return N2oSelenide.component(element().$(CSS_SELECTOR), componentClass);
    }

    @Override
    public Toolbar toolbar() {
        return N2oSelenide.collection(element().$$(".btn-toolbar .btn"), N2oToolbar.class);
    }

    @Override
    public void shouldBeRequired() {
        requiredLabel().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotBeRequired() {
        requiredLabel().shouldNotBe(Condition.exist);
    }

    @Override
    public void labelShouldHave(Condition condition) {
        element().$("label").shouldHave(condition);
    }

    @Override
    public void shouldHaveLabelLocation(FieldSet.LabelPosition position) {
        element().shouldHave(Condition.cssClass("label-" + position.getId()));
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

    private SelenideElement requiredLabel() {
        return element().$(".n2o-field-label-required");
    }
}
