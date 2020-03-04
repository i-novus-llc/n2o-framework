package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.TextArea;
import org.openqa.selenium.Keys;

/**
 * Поле ввода многострочного текста для автотестирования
 */
public class N2oTextArea extends N2oControl implements TextArea {

    @Override
    public String val() {
        return element().text();
    }

    @Override
    public void val(String value) {
        element().sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
    }

    @Override
    public void shouldHaveValue(String value) {
        element().should(value == null || value.isEmpty() ?
                Condition.empty : Condition.text(value));
    }

    @Override
    public void shouldHavePlaceholder(String value) {
        element().should(Condition.attribute("placeholder", value));
    }

}
