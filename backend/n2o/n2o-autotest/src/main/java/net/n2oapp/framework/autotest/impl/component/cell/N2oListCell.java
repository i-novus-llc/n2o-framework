package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.cell.ListCell;

import java.time.Duration;

/**
 * Ячейка таблицы со списком для автотестирования
 */
public class N2oListCell extends N2oCell implements ListCell {

    @Override
    public void shouldHaveSize(int size) {
        badges().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void shouldHaveText(int index, String val, Duration... duration) {
        should(Condition.text(val), badges().get(index), duration);
    }

    @Override
    public void shouldNotBeExpandable() {
        cellControl().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldBeExpandable() {
        cellControl().shouldBe(Condition.exist);
    }

    @Override
    public void expand() {
        cellControl().click();
    }

    protected ElementsCollection badges() {
        return element().$$(".badge");
    }

    protected SelenideElement cellControl() {
        return element().$(".collapsed-cell-control");
    }
}
