package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.component.Expandable;

/**
 * Регион с горизонтальным делителем для автотестирования
 */
public interface LineRegion extends Region, Expandable {
    /**
     * @return Элемент региона (виджет/регион) для автотестирования
     */
    RegionItems content();

    /**
     * Проверка, что регион скрываем
     */
    void shouldBeCollapsible();

    /**
     * Проверка, что регион не скрываем
     */
    void shouldNotBeCollapsible();

    /**
     * Проверка заголовка региона
     * @param title ожидаемый заголовок
     */
    void shouldHaveLabel(String title);
}
