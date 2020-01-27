package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Regions;

/**
 * Страница с левой и правой колонками для автотестирования
 */
public interface LeftRightPage extends Page {
    Regions left();
    Regions right();
}
