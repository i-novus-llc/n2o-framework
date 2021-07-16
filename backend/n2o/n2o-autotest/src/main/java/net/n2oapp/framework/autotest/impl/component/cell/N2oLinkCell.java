package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.LinkCell;

/**
 * Ячейка таблицы с ссылкой для автотестирования
 */
public class N2oLinkCell extends N2oCell implements LinkCell {

    @Override
    public void click() {
        element().scrollTo();
        element().$(".btn").click();
    }

    @Override
    public void hrefShouldHave(String href) {
        element().$("a").shouldHave(Condition.attribute("href", href));
    }

    @Override
    public void textShouldHave(String text) {
        element().shouldHave(Condition.text(text));
    }

    @Override
    public void shouldNotHaveText() {
        element().shouldHave(Condition.empty);
    }
}
