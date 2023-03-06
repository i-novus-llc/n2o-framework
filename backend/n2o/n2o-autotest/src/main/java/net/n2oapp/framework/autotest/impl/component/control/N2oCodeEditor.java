package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.control.CodeEditor;
import org.openqa.selenium.Keys;

import java.util.stream.IntStream;

/**
 * Компонент редактирования кода для автотестирования
 */
public class N2oCodeEditor extends N2oControl implements CodeEditor {

    @Override
    public void shouldBeEmpty() {
        ElementsCollection lines = element().$$(".ace_line");
        lines.shouldHave(CollectionCondition.size(1));
        lines.get(0).shouldBe(Condition.empty);
    }

    @Override
    public void setValue(String value) {
        element().$("textarea").setValue(value);
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
