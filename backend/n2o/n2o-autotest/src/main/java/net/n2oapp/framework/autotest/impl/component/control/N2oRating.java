package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
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
        String id = ratingInput().find(Condition.value(value)).shouldBe(Condition.exist).getAttribute("id");

        element().$$(".rating__label")
                .find(Condition.attribute("for", id))
                .shouldBe(Condition.exist)
                .click();
    }

    @Override
    public void shouldHaveValue(String value) {
        ratingInput().find(Condition.checked).shouldBe(Condition.exist).shouldHave(Condition.value(value));
    }

    protected ElementsCollection ratingInput() {
        return element().$$(".rating__input");
    }
}
