package net.n2oapp.framework.autotest.impl.component.snippet;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.snippet.Text;

import java.time.Duration;

/**
 * Компонент текста для автотестирования
 */
public class N2oText extends N2oSnippet implements Text {

    @Override
    public void shouldHaveText(String text, Duration... duration) {
        should(Condition.text(text), duration);
    }
}
