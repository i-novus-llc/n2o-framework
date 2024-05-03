package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Компонент пагинации для автотестирования
 */
public class N2oPaging extends N2oComponent implements Paging {

    protected String ellipsisLocator = ".ellipsis";

    protected String pageItemLocator = ".page-item";

    protected String countButtonLocator = ".pagination__total__button";

    protected String pagesLocator = ".pagination-pages";

    protected String nextButtonLocator = ".next";

    protected String prevButtonLocator = ".prev";

    protected String paginationContainer = ".pagination-container";

    protected String totalTextLocator = ".pagination__total__text";

    public N2oPaging(SelenideElement element) {
        setElement(element);
    }

    @Override
    public void shouldHaveActivePage(String label, Duration... duration) {
        SelenideElement e = element().$(paginationContainer.concat(" ").concat(pageItemLocator).concat(".active.page-link .title"));
        should(Condition.text(label), e, duration);
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
    public int totalElements() {
        String info = paginationInfo().text();
        info = info.split(" ")[2];
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
    public void shouldHavePrev() {
        prevButton().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotHavePrev() {
        prevButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void prevButtonShouldBeEnabled() {
        prevButton().shouldNotHave(Condition.cssClass("disabled"));
    }

    @Override
    public void prevButtonShouldBeDisabled() {
        prevButton().shouldHave(Condition.cssClass("disabled"));
    }

    @Override
    public void prevShouldHaveLabel(String label, Duration... duration) {
        should(Condition.text(label), prevButton().parent(), duration);
    }

    @Override
    public void prevShouldHaveIcon(String icon) {
        prevButton().$("i").shouldHave(Condition.attribute("class", icon));
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
    public void nextShouldHaveLabel(String label, Duration... duration) {
        should(Condition.text(label), nextButton().parent(), duration);
    }

    @Override
    public void nextShouldHaveIcon(String icon) {
        nextButton().$("i").shouldHave(Condition.attribute("class", icon));
    }

    @Override
    public void selectNext() {
        nextButton().click();
    }

    @Override
    public void nextButtonShouldBeEnabled() {
        nextButton().shouldNotHave(Condition.cssClass("disabled"));
    }

    @Override
    public void nextButtonShouldBeDisabled() {
        nextButton().shouldHave(Condition.cssClass("disabled"));
    }


    @Override
    public void shouldHaveFirst() {
        firstPage().shouldBe(Condition.exist);
    }

    @Override
    public void selectFirst() {
        if (firstPage().$(pageItemLocator).exists()) {
            firstPage().$(pageItemLocator).click();
        }
        else {
            firstPage().click();
        }
    }

    @Override
    public void shouldNotHaveLast() {
        lastPage().$(".title").shouldNotBe(Condition.exist);
    }

    @Override
    public void shouldHaveLast() {
        lastPage().$(".title").shouldBe(Condition.exist);
    }

    @Override
    public void lastShouldHavePage(String label, Duration... duration) {
        should(Condition.text(label), lastPage().parent(), duration);
    }

    @Override
    public void selectLast() {
        if (lastPage().$(pageItemLocator).is(Condition.exist)) {
            lastPage().$(pageItemLocator).click();
        }
        else {
            lastPage().click();
        }
    }

    @Override
    public void firstPageShouldHaveEllipsis() {
        firstPage().$(ellipsisLocator).shouldBe(Condition.visible);
    }
    @Override
    public void firstPageShouldNotHaveEllipsis() {
        firstPage().$(ellipsisLocator).shouldNotBe(Condition.exist);
    }

    @Override
    public void lastPageShouldHaveEllipsis() {
        lastPage().$(ellipsisLocator).shouldBe(Condition.visible);
    }

    @Override
    public void lastPageShouldNotHaveEllipsis() {
        lastPage().$(ellipsisLocator).shouldNotBe(Condition.exist);
    }

    @Override
    public void pageNumberButtonShouldBeVisible(String number) {
        pageNumberButton(number).shouldBe(Condition.visible);
    }

    @Override
    public void pageNumberButtonShouldNotBeVisible(String number) {
        pageNumberButton(number).shouldNotBe(Condition.visible);
    }

    @Override
    public void countButtonShouldBeVisible() {
        countButton().shouldBe(Condition.visible);
    }

    @Override
    public void countButtonShouldNotBeVisible() {
        countButton().shouldNot(Condition.exist);
    }

    @Override
    public void countButtonClick() {
        countButton().click();
    }

    @Override
    public void shouldExists() {
        element().$(paginationContainer).lastChild().shouldBe(Condition.exist);
    }

    @Override
    public void shouldNotExists() {
        if (element().$(paginationContainer).exists()) {
            element().$(paginationContainer).lastChild().shouldNotHave(Condition.exist);
        } else
            element().$(paginationContainer).shouldNotBe(Condition.exist);
    }

    protected SelenideElement pageNumberButton(String number) {
        return element().$$(paginationContainer.concat(" ").concat(pageItemLocator)).findBy(Condition.text(number));
    }

    protected SelenideElement paginationInfo() {
        return element().$(paginationContainer.concat(" ").concat(totalTextLocator));
    }

    protected SelenideElement prevButton() {
        return element().$(paginationContainer.concat(" ").concat(prevButtonLocator));
    }

    protected SelenideElement nextButton() {
        return element().$(paginationContainer.concat(" ").concat(nextButtonLocator));
    }

    protected SelenideElement firstPage() {
        return element().$$(pagesLocator.concat( " > .page-item, .extra-page-item")).first();
    }

    protected SelenideElement lastPage() {
       return element().$$(pagesLocator.concat( " > .page-item, .extra-page-item")).last();
    }

    protected SelenideElement countButton() {
        return element().$(countButtonLocator);
    }
}
