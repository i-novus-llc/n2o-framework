package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;

/**
 * Кнопка с ссылкой для автотестирования
 */
public class N2oAnchorMenuItem extends N2oMenuItem implements AnchorMenuItem {
    @Override
    public void shouldHaveIcon() {
        element().$("i").shouldHave(Condition.exist);
    }

    @Override
    public void iconShouldHaveCssClass(String clazz) {
        element().$("i").shouldHave(Condition.attributeMatching("class", ".*" + clazz));
    }

    @Override
    public void shouldHaveBadge() {
        element().$(".badge").shouldHave(Condition.exist);
    }

    @Override
    public void badgeShouldHaveValue(String value) {
        element().$(".badge").shouldHave(Condition.text(value));
    }

    @Override
    public void urlShouldHave(String url) {
        element().$("a").shouldHave(Condition.attribute("href", url));
    }
}
