package net.n2oapp.framework.autotest.impl.component.cell;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import net.n2oapp.framework.autotest.api.component.cell.RatingCell;

/**
 * Ячейка таблицы с рейтингом для автотестирования
 */
public class N2oRatingCell extends N2oCell implements RatingCell {

    @Override
    public void shouldHaveMax(int max) {
        ratingInput().last()
                .shouldHave(Condition.attribute("value", "" + max));
    }

    @Override
    public void shouldHaveValue(String value) {
        ratingInput().find(Condition.selected).shouldHave(Condition.value(value));
    }

    @Override
    public void value(String value) {
        element().$$(".rating__label")
                .find(Condition.attributeMatching("for", "rating-" + value + ".*"))
                .click();
    }

    protected ElementsCollection ratingInput() {
        if (element().$$(".rating__input--readonly").isEmpty())
            return element().$$(".rating__input");
        else
            return element().$$(".rating__input--readonly");
    }
}
