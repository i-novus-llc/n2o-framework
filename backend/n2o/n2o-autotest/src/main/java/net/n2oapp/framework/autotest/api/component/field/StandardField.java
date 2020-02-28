package net.n2oapp.framework.autotest.api.component.field;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.Control;

/**
 *  Стандартное поле формы для автотестирования
 */
public interface StandardField extends Field {
    <T extends Control> T control(Class<T> componentClass);

    void shouldBeRequired();
    void shouldNotBeRequired();
    void labelShouldHave(Condition condition);
    void messageShouldHave(Condition condition);
    void shouldHaveDescription(Condition condition);
    void shouldHaveValidationMessage(Condition condition);
}
