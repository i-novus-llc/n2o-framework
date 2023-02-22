package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.control.Rating;

/**
 * Компонент ввода и отображения рейтинга для автотестирования
 */
public class N2oRating extends N2oControl implements Rating {

    @Override
    public void shouldBeEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(String value) {
        String id = element().$$(".rating__input").find(Condition.value(value)).shouldBe(Condition.exist).getAttribute("id");
        element().$$(".rating__label").find(Condition.attribute("for", id)).shouldBe(Condition.exist).click();
    }

    @Override
    public void shouldHaveValue(String value) {
        element().$$(".rating__input").find(Condition.checked).shouldBe(Condition.exist).shouldHave(Condition.value(value));
    }
}
