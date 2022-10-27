package net.n2oapp.framework.autotest.impl.component;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.Tooltip;

/**
 * Тултип компонента страницы
 */
public class N2oTooltip extends N2oComponent implements Tooltip {

    @Override
    public void shouldBeEmpty() {
        element().shouldBe(Condition.empty);
    }

    @Override
    public void shouldHaveText(String... text) {
        String value = String.join("\n", text);
        element().shouldHave(Condition.text(value));
    }
}
