package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.TextEditor;
import org.openqa.selenium.Keys;

/**
 * Компонент редактирования текста для автотестирования
 */
public class N2oTextEditor extends N2oControl implements TextEditor {

    @Override
    public void shouldBeEmpty() {
        element().shouldHave(Condition.empty);
    }

    @Override
    public void val(String value) {
        element().$("span").setValue(value);
    }

    @Override
    public void shouldHaveValue(String value) {
        element().shouldHave(Condition.text(value));
    }
}
