package net.n2oapp.framework.autotest.impl.component.cell;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.ProgressBarCell;

/**
 * Ячейка таблицы с ProgressBar для автотестирования
 */
public class N2oProgressBarCell extends N2oCell implements ProgressBarCell {
    @Override
    public void shouldHaveColor(Colors color) {
        progressBar().shouldBe(Condition.cssClass(color.name("bg-")));
    }

    @Override
    public void shouldHaveValue(String value) {
        progressBar().shouldHave(Condition.attribute("aria-valuenow", value));
    }

    @Override
    public void shouldHaveSize(Size size) {
        progressBar()
                .parent()
                .shouldBe(Condition.cssClass(size.name()));
    }

    @Override
    public void shouldBeAnimated() {
        progressBar().shouldBe(Condition.cssClass("progress-bar-animated"));
    }

    @Override
    public void shouldBeStriped() {
        progressBar().shouldBe(Condition.cssClass("progress-bar-striped"));

    }

    protected SelenideElement progressBar() {
        return element().$(".progress-bar");
    }
}
