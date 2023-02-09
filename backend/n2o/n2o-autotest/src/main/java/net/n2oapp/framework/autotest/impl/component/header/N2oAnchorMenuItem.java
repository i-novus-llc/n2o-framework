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
    public void shouldHaveIconWithCssClass(String cssClass) {
        element().$("i").shouldHave(Condition.attributeMatching("class", ".*" + cssClass));
    }

    @Override
    public void shouldHaveBadge() {
        element().$(".badge").shouldHave(Condition.exist);
    }

    @Override
    public void shouldHaveBadgeWithText(String text) {
        element().$(".badge").shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveBadgeWithColor(String color) {
        element().$(".badge").shouldHave(Condition.cssClass("badge-" + color));
    }

    @Override
    public void shouldHaveUrl(String url) {
        element().$("a").shouldHave(Condition.attribute("href", url));
    }
}
