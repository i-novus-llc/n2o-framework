package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.OutputText;

/**
 * Компонент вывода текста (output-text) для автотестирования
 */
public class N2oOutputText extends N2oControl implements OutputText {

    public void shouldHaveValue(String value) {
        element().shouldHave(Condition.text(value));
    }

    public void shouldNotHaveValue() {
        element().$(".text").shouldNotBe(Condition.exist);
    }

    public void shouldHaveIcon(String icon) {
        element().$(".n2o-icon." + icon.replace(" ", ".")).shouldBe(Condition.exist);
    }
}
