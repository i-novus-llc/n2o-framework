package net.n2oapp.framework.autotest.impl.component.control;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.control.Rating;

import java.time.Duration;

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
    public void shouldHaveValue(String value, Duration... duration) {
        SelenideElement e = ratingInput().find(Condition.checked).shouldBe(Condition.exist);
        should(Condition.value(value), e, duration);
    }

    @Override
    public void shouldBeDisabled() {
        element().$(".rating-group--readonly").shouldBe(Condition.exist);
    }

    protected ElementsCollection ratingInput() {
        return element().$$(".rating__input");
    }
}
