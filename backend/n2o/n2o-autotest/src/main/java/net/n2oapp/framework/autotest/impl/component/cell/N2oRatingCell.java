package net.n2oapp.framework.autotest.impl.component.cell;


import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.cell.RatingCell;

/**
 * Ячейка таблицы с рейтингом для автотестирования
 */
public class N2oRatingCell extends N2oCell implements RatingCell {

    @Override
    public void maxShouldBe(int max) {
        element().$$(".rating__input").shouldHaveSize(max + 1);
        element().$$(".rating__input").last().shouldHave(Condition.attribute("value", ""+max));
    }

    @Override
    public void checkedShouldBe(int index) {
        element().$$(".rating__input").get(index).shouldBe(Condition.attribute("checked"));
    }

    @Override
    public void check(int index) {
        element().$$(".rating__label").get(index).hover().shouldBe(Condition.exist).click();
    }
}
