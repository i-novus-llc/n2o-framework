package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Regions;

/**
 * Стандартная страница для автотестирования
 */
public interface StandardPage extends Page {

    /**
     * @return Регионы для автотестирования
     */
    Regions regions();
}
