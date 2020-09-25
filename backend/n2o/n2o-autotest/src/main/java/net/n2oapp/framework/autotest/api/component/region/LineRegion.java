package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.collection.Widgets;

/**
 * Регион с горизонтальным делителем для автотестирования
 */
public interface LineRegion extends Region, Сollapsible {
    Widgets content();

    void shouldBeCollapsible();

    void shouldNotBeCollapsible();
}
