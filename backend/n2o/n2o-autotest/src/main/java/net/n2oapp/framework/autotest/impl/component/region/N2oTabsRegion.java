package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.collection.Widgets;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.impl.collection.N2oWidgets;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import static net.n2oapp.framework.autotest.N2oSelenide.collection;

public class N2oTabsRegion extends N2oRegion implements TabsRegion {
    @Override
    public TabItem tab(int index) {
        return new N2oTabItem(element().$$(".nav-item").shouldBe(CollectionCondition.sizeGreaterThan(index)).get(index));
    }

    @Override
    public void shouldHaveSize(int size) {
        element().$$(".nav-item").shouldHaveSize(size);
    }

    @Override
    public TabItem tab(Condition by) {
        return new N2oTabItem(element().$$(".nav-item").findBy(by));
    }

    @Override
    public Widgets activeTab() {
        return collection(element().$$("div.tab-pane.active div.n2o-standard-widget-layout"), N2oWidgets.class);
    }

    public static class N2oTabItem extends N2oComponent implements TabItem {

        public N2oTabItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void click() {
            element().click();
        }

        @Override
        public void shouldHaveText(String text) {
            element().shouldHave(Condition.text(text));
        }

        @Override
        public void shouldBeActive() {
            element().$(".nav-link").shouldHave(Condition.cssClass("active"));
        }

        @Override
        public void shouldNotBeActive() {
            element().$(".nav-link").shouldNotHave(Condition.cssClass("active"));
        }
    }
}
