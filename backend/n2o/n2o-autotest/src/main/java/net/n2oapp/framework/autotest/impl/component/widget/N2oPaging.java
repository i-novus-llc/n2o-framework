package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
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
        getItems().findBy(Condition.text(number)).click();
    }

    @Override
    public void pagingShouldHave(String number) {
        getItems().findBy(Condition.text(number)).shouldBe(Condition.exist);
    }

    @Override
    public int totalElements() {
        String info = getPaginationInfo().text();
        info = info.split(" ")[1];
        return Integer.parseInt(info);
    }

    @Override
    public void totalElementsShouldBe(int count) {
        getPaginationInfo().scrollTo().should(Condition.matchesText("" + count));
    }

    @Override
    public void totalElementsShouldNotExist() {
        getPaginationInfo().shouldNotBe(Condition.exist);
    }

    @Override
    public void prevShouldNotExist() {
        getItems().findBy(Condition.text("‹")).shouldNotBe(Condition.exist);
    }

    @Override
    public void prevShouldExist() {
        getItems().findBy(Condition.text("‹")).shouldBe(Condition.exist);
    }

    @Override
    public void nextShouldNotExist() {
        getItems().findBy(Condition.text("›")).shouldNotBe(Condition.exist);
    }

    @Override
    public void nextShouldExist() {
        getItems().findBy(Condition.text("›")).shouldBe(Condition.exist);
    }

    @Override
    public void lastShouldNotExist() {
        getItems().findBy(Condition.text("»")).shouldNotBe(Condition.exist);
    }

    @Override
    public void lastShouldExist() {
        getItems().findBy(Condition.text("»")).shouldBe(Condition.exist);
    }

    @Override
    public void firstShouldNotExist() {
        getItems().findBy(Condition.text("«")).shouldNotBe(Condition.exist);
    }

    @Override
    public void firstShouldExist() {
        getItems().findBy(Condition.text("«")).shouldBe(Condition.exist);
    }

    private ElementsCollection getItems() {
        return element().$$(".n2o-pagination .page-item .page-link");
    }

    private SelenideElement getPaginationInfo() {
        return element().$(".n2o-pagination .n2o-pagination-info");
    }
}
