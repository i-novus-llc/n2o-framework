package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Expandable;

/**
 * Регион в виде панели для автотестирования
 */
public interface PanelRegion extends Region, Expandable {
    RegionItems content();

    void shouldHaveTitle(String title);

    void shouldNotHaveTitle();

    void shouldHaveFooterTitle(String footer);

    void shouldBeCollapsible();

    void shouldNotBeCollapsible();

    void shouldHaveBorderColor(Colors color);

    void shouldHaveIcon(String icon);
}
