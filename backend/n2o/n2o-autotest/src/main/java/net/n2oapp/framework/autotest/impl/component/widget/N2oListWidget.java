package net.n2oapp.framework.autotest.impl.component.widget;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.ListWidget;

public class N2oListWidget extends N2oStandardWidget implements ListWidget {
    @Override
    public Content content(int index) {
        return new N2oContent(element().$$(".n2o-widget-list-item").get(index));
    }

    @Override
    public void shouldHaveSize(int size) {
        element().$$(".n2o-widget-list-item").shouldHaveSize(size);
    }

    public static class N2oContent implements Content {

        private final SelenideElement element;

        public N2oContent(SelenideElement element) {
            this.element = element;
        }

        @Override
        public void bodyShouldHaveText(String text) {
            element.$(".n2o-widget-list-item-body").shouldHave(Condition.text(text));
        }
    }
}
