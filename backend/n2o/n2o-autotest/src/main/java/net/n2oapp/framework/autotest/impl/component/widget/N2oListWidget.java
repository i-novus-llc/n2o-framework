package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.cell.Cell;
import net.n2oapp.framework.autotest.api.component.widget.Paging;
import net.n2oapp.framework.autotest.api.component.widget.list.ListWidget;

public class N2oListWidget extends N2oStandardWidget implements ListWidget {
    @Override
    public Content content(int index) {
        return new N2oContent(items().get(index));
    }

    @Override
    public void shouldHaveSize(int size) {
        items().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public Paging paging() {
        return new N2oPaging(element());
    }

    protected ElementsCollection items() {
        return element().$$(".n2o-widget-list-item");
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
}
