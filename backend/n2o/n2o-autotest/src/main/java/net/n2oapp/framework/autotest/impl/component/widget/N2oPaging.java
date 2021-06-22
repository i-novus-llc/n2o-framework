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
    public void activePageShouldBe(String label) {
        element().$(".n2o-pagination .page-item.active .page-link").shouldHave(Condition.text(label));
    }

    @Override
    public void selectPage(String number) {
        pageNumberButton(number).click();
    }

    @Override
    public void pagingShouldHave(String number) {
        pageNumberButton(number).shouldBe(Condition.exist);
    }

    @Override
    public int totalElements() {
        String info = paginationInfo().text();
        info = info.split(" ")[1];
        return Integer.parseInt(info);
    }

    @Override
    public void totalElementsShouldBe(int count) {
        paginationInfo().scrollTo().should(Condition.matchesText("" + count));
    }

    @Override
    public void totalElementsShouldNotExist() {
        paginationInfo().shouldNotBe(Condition.exist);
    }

    @Override
    public void prevShouldNotExist() {
        prevButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void prevShouldExist() {
        prevButton().shouldBe(Condition.exist);
    }

    @Override
    public void selectPrev() {
        prevButton().click();
    }

    @Override
    public void nextShouldNotExist() {
        nextButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void nextShouldExist() {
        nextButton().shouldBe(Condition.exist);
    }

    @Override
    public void selectNext() {
        nextButton().click();
    }

    @Override
    public void firstShouldNotExist() {
        firstButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void firstShouldExist() {
        firstButton().shouldBe(Condition.exist);
    }

    @Override
    public void selectFirst() {
        firstButton().click();
    }

    @Override
    public void lastShouldNotExist() {
        lastButton().shouldNotBe(Condition.exist);
    }

    @Override
    public void lastShouldExist() {
        lastButton().shouldBe(Condition.exist);
    }

    @Override
    public void selectLast() {
        lastButton().click();
    }


    private SelenideElement pageNumberButton(String number) {
        return element().$$(".n2o-pagination .page-item .page-link").findBy(Condition.text(number));
    }

    private SelenideElement paginationInfo() {
        return element().$(".n2o-pagination .n2o-pagination-info");
    }

    private SelenideElement navigateButton(String iconClass) {
        return element().$$(".n2o-pagination .page-link i").findBy(Condition.cssClass(iconClass));
    }

    private SelenideElement prevButton() {
        return navigateButton("fa-angle-left");
    }

    private SelenideElement nextButton() {
        return navigateButton("fa-angle-right");
    }

    private SelenideElement firstButton() {
        return navigateButton("fa-angle-double-left");
    }

    private SelenideElement lastButton() {
        return navigateButton("fa-angle-double-right");
    }
}
