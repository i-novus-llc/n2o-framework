package net.n2oapp.framework.autotest.impl.component.field;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.field.Field;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Поле формы для автотестирования
 */
public class N2oField extends N2oComponent implements Field {
    @Override
    public void shouldHaveLabel(String label, Duration... duration) {
        should(Condition.text(label), label(), duration);
    }

    @Override
    public void shouldNotHaveLabel() {
        label().shouldNot(Condition.exist);
    }

    @Override
    public void shouldHaveEmptyLabel() {
        label().should(Condition.empty);
    }

    protected SelenideElement label() {
        return element().$(".n2o-field-label");
    }
}
