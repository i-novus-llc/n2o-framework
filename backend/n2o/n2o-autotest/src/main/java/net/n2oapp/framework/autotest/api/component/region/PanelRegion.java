package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.collection.Widgets;

/**
 * Регион в виде панели для автотестирования
 */
public interface PanelRegion extends Region, Сollapsible {
    Widgets content();

    void shouldHaveTitle(String title);

    void shouldNotHaveTitle();

    void shouldHaveFooterTitle(String footer);

    void expandContent();

    void collapseContent();

    void shouldBeCollapsible();

    void shouldNotBeCollapsible();

    void shouldHaveBorderColor(Colors color);

    void shouldHaveIcon(String icon);
}
