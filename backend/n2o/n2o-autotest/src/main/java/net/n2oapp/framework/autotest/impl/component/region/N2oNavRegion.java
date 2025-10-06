package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.region.NavRegion;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oNavRegion extends N2oRegion implements NavRegion {

    @Override
    public RegionItems content() {
        return new N2oRegionItems(element().$$(".n2o-navigation-panel__children"));
    }

    public static class N2oRegionItems implements RegionItems {
        private final ElementsCollection elements;

        public N2oRegionItems(ElementsCollection elements) {
            this.elements = elements;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends NavRegionItem> T item(int index, Class<T> clazz) {
            return (T) createItem(elements.get(index));
        }

        @Override
        public void shouldHaveSize(int size) {
            elements.shouldHave(CollectionCondition.size(size));
        }

        private NavRegionItem createItem(SelenideElement element) {
            if (element.is(Condition.tagName("a"))) {
                return new N2oAnchorItem(element);
            } else if (element.has(Condition.cssClass("group"))) {
                return new N2oGroupItem(element);
            } else if (element.has(Condition.cssClass("dropdown"))) {
                return new N2oDropdownItem(element);
            } else {
                return new N2oNavRegionItem(element);
            }
        }
    }

    public static class N2oNavRegionItem extends N2oComponent implements NavRegionItem {
        public N2oNavRegionItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveLabel(String label) {
            element().shouldHave(Condition.or("label", Condition.text(label), Condition.cssClass("n2o-icon-container").because("No label found")));
        }

        @Override
        public void click() {
            element().$(".btn").click();
        }
    }

    public static class N2oAnchorItem extends N2oNavRegionItem implements AnchorItem {
        public N2oAnchorItem(SelenideElement element) {
            super(element);
        }

        @Override
        public void shouldHaveLabel(String label) {
            element().shouldHave(Condition.text(label));
        }

        @Override
        public void shouldHaveUrl(String url) {
            element().shouldHave(Condition.attribute("href", url));
        }

        @Override
        public void click() {
            element().click();
        }
    }

    public static class N2oGroupItem extends N2oNavRegionItem implements GroupItem {
        public N2oGroupItem(SelenideElement element) {
            super(element);
        }

        @Override
        public RegionItems items() {
            return new N2oRegionItems(element().$(".group-children").$$(":scope > .group-children__child"));
        }

        @Override
        public void shouldHaveSize(int size) {
            items().shouldHaveSize(size);
        }

        @Override
        public void shouldHaveLabel(String label) {
            element().$(".group-label").shouldHave(Condition.text(label));
        }

        @Override
        public <T extends NavRegionItem> T item(int index, Class<T> clazz) {
            return items().item(index, clazz);
        }

        @Override
        public void click() {
            element().click();
        }
    }

    public static class N2oDropdownItem extends N2oNavRegionItem implements DropdownItem {
        public N2oDropdownItem(SelenideElement element) {
            super(element);
        }

        @Override
        public RegionItems items() {
            return new N2oRegionItems(element().$(".dropdown-menu").$$(":scope > .group-children__child"));
        }

        @Override
        public void shouldHaveSize(int size) {
            items().shouldHaveSize(size);
        }

        @Override
        public void shouldHaveLabel(String label) {
            element().$(".dropdown-toggle").shouldHave(Condition.text(label));
        }

        @Override
        public <T extends NavRegionItem> T item(int index, Class<T> clazz) {
            return items().item(index, clazz);
        }

        @Override
        public void click() {
            element().click();
        }
    }
}