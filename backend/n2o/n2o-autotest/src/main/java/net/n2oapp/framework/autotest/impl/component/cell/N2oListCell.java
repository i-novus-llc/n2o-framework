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
    public void shouldHaveInnerLinksSize(int size) {
        innerLinks().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void shouldHaveInnerBadgesSize(int size) {
        innerBadges().shouldHave(CollectionCondition.size(size));
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


    @Override
    public void shouldHaveInnerText(int index, String val, Duration... duration) {
        should(Condition.text(val), innerTexts().get(index), duration);
    }

    @Override
    public void shouldHaveInnerLink(int index, String val, Duration... duration) {
        should(Condition.text(val), innerLinks().get(index), duration);
    }

    @Override
    public void shouldHaveInnerBadge(int index, String val, Duration... duration) {
        should(Condition.text(val), innerBadges().get(index), duration);
    }
    @Override
    public void shouldHaveSeparator(String val, Duration... duration) {
        should(Condition.text(val), cellContent(), duration);
    }

    @Override
    public void shouldBeInline() {
        cellContentInline().should(Condition.exist);
    }

    @Override
    public void shouldNotBeInline() {
        cellContentInline().should(Condition.not(Condition.exist));
    }

    @Override
    public void shouldHaveHref(int index, String href){
        innerLinks().get(index).shouldHave(Condition.attribute("href", href));
    }

    protected ElementsCollection badges() {
        return element().$$(".badge");
    }

    protected SelenideElement cellControl() {
        return element().$(".collapsed-cell-control");
    }

    protected SelenideElement cellContent() {
        return element().$(".collapse-cell-content");
    }

    protected SelenideElement cellContentInline() {
        return element().$(".collapse-cell-content--inline");
    }

    protected ElementsCollection innerTexts() {
        return element().$$(".icon-cell-container");
    }

    protected ElementsCollection innerLinks() {
        return element().$$(".n2o-link-cell");
    }

    protected ElementsCollection innerBadges() { return element().$$(".n2o-badge"); }

}
