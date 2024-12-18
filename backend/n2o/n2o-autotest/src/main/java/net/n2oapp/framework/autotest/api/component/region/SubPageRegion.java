package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.component.page.Page;

/**
 * Регион подстраниц для автотестирования
 */
public interface SubPageRegion extends Region {

    /**
     * Возвращает страницу внутри <sub-page>
     * @param pageClass тип возвращаемой страницы
     * @return компонент страница для автотестирования
     */
    <T extends Page> T content(Class<T> pageClass);
}
