package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.control.CodeEditor;

import java.time.Duration;
import java.util.stream.IntStream;

/**
 * Компонент редактирования кода для автотестирования
 */
public class N2oCodeEditor extends N2oControl implements CodeEditor {

    @Override
    public void shouldBeEmpty() {
        ElementsCollection lines = lines();
        lines.shouldHave(CollectionCondition.size(1));
        lines.get(0).shouldBe(Condition.empty);
    }

    @Override
    public void setValue(String value) {
        element().$("textarea").setValue(value);
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        String[] lines = value.trim().split("\n");
        IntStream.range(0, lines.length).forEach(i -> shouldHaveValue(lines[i], i, duration));
    }

    @Override
    public void shouldHaveValue(String value, int line, Duration... duration) {
        should(Condition.text(value), lines().get(line), duration);
    }

    @Override
    public void shouldHaveLines(int lines) {
        lines().shouldHave(CollectionCondition.size(lines));
    }

    @Override
    public void shouldHaveRowsInLine(int rows, int line) {
        lines().get(line).$$(".ace_line").shouldHave(CollectionCondition.size(rows));
    }

    @Override
    public void shouldBeDisabled() {
        element().$(".ace_text-input").shouldHave(Condition.readonly);
    }

    protected ElementsCollection lines() {
        return element().$$(".ace_line_group");
    }
}
