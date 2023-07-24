package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.LinkCell;

import java.time.Duration;

/**
 * Ячейка таблицы с ссылкой для автотестирования
 */
public class N2oLinkCell extends N2oCell implements LinkCell {

    @Override
    public void click() {
        element().scrollTo().$(".btn").click();
    }

    @Override
    public void shouldHaveHref(String href) {
        element().$("a").shouldHave(Condition.attribute("href", href));
    }

    @Override
    public void shouldHaveText(String text, Duration... duration) {
        should(Condition.exactText(text), duration);
    }

    @Override
    public void shouldNotHaveText(Duration... duration) {
        should(Condition.empty, duration);
    }
}
