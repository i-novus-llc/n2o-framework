package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Регион с автоматически прокручиваемым меню
 */
public interface ScrollspyRegion extends Region {

    ContentItem contentItem(int index);

    ContentItem contentItem(String title);

    void shouldHaveActiveContentItem(String title);

    void shouldHaveActiveMenuItem(String title);

    void shouldHaveMenuOnPosition(MenuPosition position);

    Menu menu();

    interface ContentItem extends Region {

        RegionItems content();
    }

    interface Menu extends Component {

        void shouldHaveTitle(String title);

        MenuItem menuItem(int index);

        MenuItem menuItem(String title);

        DropdownMenuItem dropdownMenuItem(int index);

        DropdownMenuItem dropdownMenuItem(String title);
    }

    interface MenuItem extends Component {

        void shouldHaveText(String text);

        void click();
    }

    interface DropdownMenuItem extends MenuItem {

        MenuItem menuItem(int index);

        MenuItem menuItem(String title);
    }

    enum MenuPosition {
        left,
        right
    }
}
