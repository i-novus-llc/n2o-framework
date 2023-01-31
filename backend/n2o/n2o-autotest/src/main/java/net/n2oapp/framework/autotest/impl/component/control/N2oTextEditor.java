package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.TextEditor;

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
        SelenideElement element = element().$(".public-DraftEditor-content");
        element.click();
        element.setValue(value);
    }

    @Override
    public void shouldHaveValue(String value) {
        element().shouldHave(Condition.text(value));
    }
}
