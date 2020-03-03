package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.CodeEditor;
import org.openqa.selenium.Keys;

import java.util.stream.IntStream;

/**
 * Компонент редактирования кода (code-editor) для автотестирования
 */
public class N2oCodeEditor extends N2oControl implements CodeEditor {

    @Override
    public void val(String value) {
        element().$("textarea").sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
    }

    @Override
    public void shouldHaveValue(String value) {
        String[] lines = value.trim().split("\n");
        IntStream.range(0, lines.length).forEach(i -> shouldHaveValue(lines[i], i));
    }

    @Override
    public void shouldHaveValue(String value, int line) {
        element().$$(".ace_line").get(line).shouldHave(Condition.text(value));
    }
}
