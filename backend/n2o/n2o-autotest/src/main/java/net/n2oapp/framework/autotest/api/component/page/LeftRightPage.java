package net.n2oapp.framework.autotest.api.component.page;

import net.n2oapp.framework.autotest.api.collection.Regions;

/**
 * Страница с левой и правой колонками для автотестирования
 */
public interface LeftRightPage extends Page {

    /**
     * Возвращает левую часть страницы как группу регионов
     * @return Группа коомпонентов регионы для автотестирования
     */
    Regions left();

    /**
     * Возвращает правую часть страницы как группу регионов
     * @return Группа коомпонентов регионы для автотестирования
     */
    Regions right();
}
