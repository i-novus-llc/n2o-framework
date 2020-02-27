package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;

/**
 * Кнопка с ссылкой для автотестирования
 */
public class N2oAnchorMenuItem extends N2oMenuItem implements AnchorMenuItem {
    @Override
    public void urlShouldHave(String url) {
        element().$("a").shouldHave(Condition.attribute("href", url));
    }
}
