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
        element().$$(".n2o-pagination .page-item .page-link").findBy(Condition.text(number)).click();
    }

    @Override
    public void pagingShouldHave(String number) {
        element().$$(".n2o-pagination .page-item .page-link").findBy(Condition.text(number)).shouldBe(Condition.exist);
    }

    @Override
    public int totalElements() {
        String info = element().$(".n2o-pagination .n2o-pagination-info").text();
        info = info.split(" ")[1];
        return Integer.parseInt(info);
    }

    @Override
    public void totalElementsShouldBe(int count) {
        element().$(".n2o-pagination .n2o-pagination-info").scrollTo().should(Condition.matchesText("" + count));
    }
}
