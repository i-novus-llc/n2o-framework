package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.OutputText;

/**
 * Компонент вывода однострочного текста для автотестирования
 */
public class N2oOutputText extends N2oControl implements OutputText {

    @Override
    public void shouldBeEmpty() {
        SelenideElement text = text();

        if (text.exists())
            text.shouldBe(Condition.empty);
    }

    @Override
    public void shouldNotBeEmpty() {
        SelenideElement text = text();

        if (text.exists())
            text.shouldNotBe(Condition.empty);
    }

    @Override
    public void shouldHaveValue(String value) {
        element().shouldHave(Condition.text(value));
    }

    @Override
    public void shouldHaveIcon(String icon) {
        element().$(String.format(".n2o-icon.%s", icon.replace(" ", "."))).shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHaveValue(String value) {
        element().shouldNotHave(Condition.text(value));
    }

    @Override
    public String getValue() {
        return text().text();
    }

    protected SelenideElement text() {
        return element().shouldBe(Condition.exist).$(".text");
    }
}
