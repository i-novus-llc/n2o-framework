package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.header.AnchorMenuItem;

/**
 * Кнопка с ссылкой для автотестирования
 */
public class N2oAnchorMenuItem extends N2oMenuItem implements AnchorMenuItem {
    @Override
    public void shouldHaveIcon() {
        icon().shouldHave(Condition.exist);
    }

    @Override
    public void shouldHaveIconCssClass(String cssClass) {
        icon().shouldHave(Condition.attributeMatching("class", String.format(".*%s", cssClass)));
    }

    @Override
    public void shouldHaveBadge() {
        //ToDo: можно ли использовать методы Badge.class?
        badge().shouldHave(Condition.exist);
    }

    @Override
    public void shouldHaveBadgeText(String text) {
        badge().shouldHave(Condition.text(text));
    }

    @Override
    public void shouldHaveBadgeColor(String color) {
        badge().shouldHave(Condition.cssClass(String.format("badge-%s", color)));
    }

    @Override
    public void shouldHaveUrl(String url) {
        element().$("a").shouldHave(Condition.attribute("href", url));
    }

    protected SelenideElement icon() {
        return element().$("i");
    }

    protected SelenideElement badge() {
        return element().$(".badge");
    }
}
