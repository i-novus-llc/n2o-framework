package net.n2oapp.framework.autotest.api.component.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.control.Control;

/**
 * Стандартное поле формы для автотестирования
 */
public interface StandardField extends Field {
    <T extends Control> T control(Class<T> componentClass);

    Toolbar toolbar();

    void shouldBeRequired();

    void shouldNotBeRequired();

    void labelShouldHave(Condition condition);

    void shouldHaveLabelLocation(FieldSet.LabelPosition position);

    void messageShouldHave(Condition condition);

    void shouldHaveDescription(Condition condition);

    void shouldHaveValidationMessage(Condition condition);

}
