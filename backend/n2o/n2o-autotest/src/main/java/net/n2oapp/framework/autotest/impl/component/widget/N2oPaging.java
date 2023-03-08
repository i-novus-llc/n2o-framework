package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Компонент пагинации для автотестирования
 */
public class N2oPaging extends N2oComponent implements Paging {
    public N2oPaging(SelenideElement element) {
        setElement(element);
    }

    @Override
    public void shouldHaveActivePage(String label) {
        element().$(".n2o-pagination .page-item.active .page-link").shouldHave(Condition.text(label));
    }

    @Override
    public void selectPage(String number) {
        pageNumberButton(number).click();
    }

    @Override
    public void shouldHavePageNumber(String number) {
        pageNumberButton(number).shouldBe(Condition.exist);
    }

    @Override
    public void shouldHaveLayout(Layout layout) {
        element().$(".n2o-pagination .pagination").shouldHave(Condition.cssClass(layout.getTitle()));
    }

    @Override
    public int totalElements() {
        String info = paginationInfo().text();
        info = info.split(" ")[1];
        return Integer.parseInt(info);
    }

    @Override
    public void shouldHaveTotalElements(int count) {
        paginationInfo().scrollTo().should(Condition.matchText(String.valueOf(count)));
    }

    @Override
    public void shouldNotHaveTotalElements() {
        paginationInfo().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldNotHavePrev() {
        prevButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldHavePrev() {
        prevButton().shouldBe(Condition.exist);
    }

    @Override
    public void prevShouldHaveLabel(String label) {
        prevButton().parent().shouldHave(Condition.text(label));
    }

    @Override
    public void prevShouldHaveIcon(String icon) {
        prevButton().shouldHave(Condition.cssClass(icon));
    }

    @Override
    public void selectPrev() {
        prevButton().click();
    }

    @Override
    public void shouldNotHaveNext() {
        nextButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldHaveNext() {
        nextButton().shouldBe(Condition.exist);
    }

    @Override
    public void nextShouldHaveLabel(String label) {
        nextButton().parent().shouldHave(Condition.text(label));
    }

    @Override
    public void nextShouldHaveIcon(String icon) {
        nextButton().shouldHave(Condition.cssClass(icon));
    }

    @Override
    public void selectNext() {
        nextButton().click();
    }

    @Override
    public void shouldNotHaveFirst() {
        firstButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldHaveFirst() {
        firstButton().shouldBe(Condition.exist);
    }

    @Override
    public void firstShouldHaveLabel(String label) {
        firstButton().parent().shouldHave(Condition.text(label));
    }

    @Override
    public void firstShouldHaveIcon(String icon) {
        firstButton().shouldHave(Condition.cssClass(icon));
    }

    @Override
    public void selectFirst() {
        firstButton().click();
    }

    @Override
    public void shouldNotHaveLast() {
        lastButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldHaveLast() {
        lastButton().shouldBe(Condition.exist);
    }

    @Override
    public void lastShouldHaveLabel(String label) {
        lastButton().parent().shouldHave(Condition.text(label));
    }

    @Override
    public void lastShouldHaveIcon(String icon) {
        lastButton().shouldHave(Condition.cssClass(icon));
    }

    @Override
    public void selectLast() {
        lastButton().click();
    }


    protected SelenideElement pageNumberButton(String number) {
        return element().$$(".n2o-pagination .page-link").findBy(Condition.text(number));
    }

    protected SelenideElement paginationInfo() {
        return element().$(".n2o-pagination .n2o-pagination-total");
    }

    protected SelenideElement prevButton() {
        return element().$(".n2o-pagination .page-link .previous-button");
    }

    protected SelenideElement nextButton() {
        return element().$(".n2o-pagination .page-link .next-button");
    }

    protected SelenideElement firstButton() {
        return element().$(".n2o-pagination .page-link .first-button");
    }

    protected SelenideElement lastButton() {
        return element().$(".n2o-pagination .page-link .last-button");
    }
}
