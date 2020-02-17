package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.collection.Widgets;

/**
 * Простой регион для автотестирования
 */
public interface SimpleRegion extends Region {
    Widgets content();
}
