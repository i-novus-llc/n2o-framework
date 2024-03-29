package net.n2oapp.framework.autotest.impl.component.cell;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.cell.RatingCell;

import java.time.Duration;

/**
 * Ячейка таблицы с рейтингом для автотестирования
 */
public class N2oRatingCell extends N2oCell implements RatingCell {

    @Override
    public void shouldHaveMax(int max) {
        ratingInput().last()
                .shouldHave(Condition.attribute("value", String.valueOf(max)));
    }

    @Override
    public void shouldHaveValue(String value, Duration... duration) {
        should(Condition.value(value), ratingInput().find(Condition.selected), duration);
    }

    @Override
    public void value(String value) {
        element().$$(".rating__label")
                .find(Condition.attributeMatching("for", String.format("rating-%s.*", value)))
                .click();
    }

    protected ElementsCollection ratingInput() {
        ElementsCollection ratings = element().$$(".rating__input--readonly");
        if (ratings.isEmpty())
            return element().$$(".rating__input");
        else
            return ratings;
    }
}
