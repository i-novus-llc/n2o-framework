package net.n2oapp.framework.autotest.impl.component.region;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.ScrollspyRegion;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Регион с отслеживанием прокрутки для автотестирования
 */
public class N2oScrollspyRegion extends N2oRegion implements ScrollspyRegion {

    @Override
    public ContentItem contentItem(int index) {
        return new N2oContentItem(contentItems().get(index));
    }

    @Override
    public ContentItem contentItem(String title) {
        return new N2oContentItem(contentItems().findBy(Condition.text(title)));
    }

    @Override
    public Menu menu() {
        return new N2oMenu(element().$(".n2o-scroll-spy-region__menu-wrapper"));
    }

    @Override
    public void activeContentItemShouldHaveTitle(String title) {
        contentItems().findBy(Condition.cssClass("active")).shouldHave(Condition.text(title));
    }

    @Override
    public void activeMenuItemShouldHaveTitle(String title) {
        menu().element().$(".n2o-scroll-spy-region__menu-item.active").shouldHave(Condition.text(title));
    }

    @Override
    public void menuShouldHavePosition(MenuPosition position) {
        SelenideElement element = element().parent().$(".position-right");
        switch (position) {
            case left:
                element.shouldNotBe(Condition.exist);
                break;
            case right:
                element.shouldHave(Condition.exist);
                break;
        }
    }

    public static class N2oContentItem extends N2oRegion implements ContentItem {

        public N2oContentItem(SelenideElement element) {
            setElement(element);
        }
        @Override
        public RegionItems content() {
            return N2oSelenide.collection(firstLevelElements(".n2o-scroll-spy-region__content", "div"), RegionItems.class);
        }


    }
    public static class N2oMenu extends N2oComponent implements Menu {

        public N2oMenu(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveTitle(String title) {
            element().$(".n2o-scroll-spy-region__menu-title").shouldHave(Condition.text(title));
        }

        @Override
        public MenuItem menuItem(int index) {
            return new N2oMenuItem(menuItems().get(index));
        }

        @Override
        public MenuItem menuItem(String title) {
            return new N2oMenuItem(menuItems().findBy(Condition.text(title)));
        }

        @Override
        public DropdownMenuItem dropdownMenuItem(int index) {
            return new N2oDropdownMenuItem(dropdownItems().get(index));
        }

        @Override
        public DropdownMenuItem dropdownMenuItem(String label) {
            return new N2oDropdownMenuItem(dropdownItems().findBy(Condition.text(label)));
        }

        protected ElementsCollection dropdownItems() {
            return element().$$(".n2o-scroll-spy-region__dropdown-menu-items-wrapper");
        }

        protected ElementsCollection menuItems() {
            return element().$$(".n2o-scroll-spy-region__menu-item");
        }

    }
    public static class N2oMenuItem extends N2oComponent implements MenuItem {

        public N2oMenuItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveText(String text) {
            element().shouldHave(Condition.text(text));
        }

        @Override
        public void click() {
            element().click();
        }

    }

    public static class N2oDropdownMenuItem extends N2oComponent implements DropdownMenuItem {

        public N2oDropdownMenuItem(SelenideElement element) {
            setElement(element);
        }

        @Override
        public void shouldHaveText(String text) {
            element().shouldHave(Condition.text(text));
        }

        @Override
        public MenuItem menuItem(int index) {
            return new N2oMenuItem(items().get(index));
        }

        @Override
        public MenuItem menuItem(String title) {
            return new N2oMenuItem(items().findBy(Condition.text(title)));
        }

        @Override
        public void click() {
            element().$(".n2o-scroll-spy-region__dropdown-toggle").click();
        }

        protected ElementsCollection items() {
            return element().$$(".n2o-scroll-spy-region__menu-item");
        }

    }

    protected ElementsCollection contentItems() {
        return element().$$(".n2o-scroll-spy-region__content-wrapper");
    }
}
