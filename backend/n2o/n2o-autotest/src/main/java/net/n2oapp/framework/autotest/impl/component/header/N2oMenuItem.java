package net.n2oapp.framework.autotest.impl.component.header;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.header.MenuItem;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Базовый класс кнопки в меню хедера для автотестирования
 */
public abstract class N2oMenuItem extends N2oComponent implements MenuItem {

    @Override
    public void shouldHaveLabel(String text) {
        element().shouldHave(Condition.text(text));
    }

    @Override
    public void click() {
        element().shouldBe(Condition.exist).click();
    }
}
