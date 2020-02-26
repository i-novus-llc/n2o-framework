package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.header.Link;

/**
 * Кнопка с ссылкой для автотестирования
 */
public class N2oLink extends N2oMenuItem implements Link {
    @Override
    public void shouldHaveUrl(String url) {
        element().shouldHave(Condition.attribute("href", url));
    }
}
