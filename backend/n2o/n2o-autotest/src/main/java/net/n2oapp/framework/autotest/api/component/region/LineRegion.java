package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.collection.Widgets;

public interface LineRegion extends Region, Сollapsible {
    Widgets content();

    void shouldHaveTitle(String title);
}
