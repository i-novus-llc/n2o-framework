package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.api.component.widget.list.ListWidget;

public class N2oListWidget extends N2oStandardWidget implements ListWidget {
    @Override
    public Content content(int index) {
        return new N2oContent(element().$$(".n2o-widget-list-item").get(index));
    }

    @Override
    public void shouldHaveSize(int size) {
        element().$$(".n2o-widget-list-item").shouldHaveSize(size);
    }

    @Override
    public Paging paging() {
        return new N2oPaging();
    }

    public static class N2oContent implements Content {

        private final SelenideElement element;

        public N2oContent(SelenideElement element) {
            this.element = element;
        }

        @Override
        public void click() {
            element.$(".n2o-widget-list-item-body").shouldHave(Condition.exist).click();
        }

        @Override
        public <T extends Cell> T leftTop(Class<T> clazz) {
            return N2oSelenide.component(element.$(".n2o-widget-list-item-left-top"), clazz);
        }

        @Override
        public <T extends Cell> T leftBottom(Class<T> clazz) {
            return N2oSelenide.component(element.$(".n2o-widget-list-item-left-bottom"), clazz);
        }

        @Override
        public <T extends Cell> T header(Class<T> clazz) {
            return N2oSelenide.component(element.$(".n2o-widget-list-item-header"), clazz);
        }

        @Override
        public <T extends Cell> T body(Class<T> clazz) {
            return N2oSelenide.component(element.$(".n2o-widget-list-item-body"), clazz);
        }

        @Override
        public <T extends Cell> T subHeader(Class<T> clazz) {
            return N2oSelenide.component(element.$(".n2o-widget-list-item-subheader"), clazz);
        }

        @Override
        public <T extends Cell> T rightTop(Class<T> clazz) {
            return N2oSelenide.component(element.$(".n2o-widget-list-item-right-top"), clazz);
        }

        @Override
        public <T extends Cell> T rightBottom(Class<T> clazz) {
            return N2oSelenide.component(element.$(".n2o-widget-list-item-right-bottom"), clazz);
        }

        @Override
        public <T extends Cell> T extra(Class<T> clazz) {
            return N2oSelenide.component(element.$(".n2o-widget-list-item-extra"), clazz);
        }
    }

    public class N2oPaging implements Paging {

        @Override
        public void clickPrev() {
            element().$$(".pagination .page-link").findBy(Condition.text("‹")).click();
        }

        @Override
        public void clickNext() {
            element().$$(".pagination .page-link").findBy(Condition.text("›")).click();
        }

        @Override
        public void totalElementsShouldBe(int count) {
            getPaginationInfo().shouldHave(Condition.text(String.valueOf(count))).click();
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
}
