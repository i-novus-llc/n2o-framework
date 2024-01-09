package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.TextEditor;

import java.time.Duration;

/**
 * Компонент редактирования текста для автотестирования
 */
public class N2oTextEditor extends N2oControl implements TextEditor {

    @Override
    public void shouldBeEmpty() {
        element().shouldHave(Condition.empty);
    }

    @Override
    public void setValue(String value) {
        editor().setValue(value);
    }

    @Override
    public void click() {
        editor().click();
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        should(Condition.text(value), duration);
    }

    @Override
    public void shouldBeDisabled() {
        element().parent().shouldHave(Condition.cssClass("n2o-text-editor-wrapper--disabled"));
    }

    protected SelenideElement editor() {
        return element().$(".public-DraftEditor-content");
    }
}
