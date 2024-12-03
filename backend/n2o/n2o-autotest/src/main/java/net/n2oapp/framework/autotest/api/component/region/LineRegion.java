package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.api.component.Expandable;

import java.time.Duration;

/**
 * Регион с горизонтальным делителем для автотестирования
 */
public interface LineRegion extends Region, Expandable {
    /**
     * @return Элемент региона (виджет/регион) для автотестирования
     */
    RegionItems content();

    /**
     * @param className имя класса, по которому будет производиться поиск элементов региона.
     * @return Элемент региона (виджет/регион), соответствующий указанному классу
     */
    RegionItems content(String className);

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
    void shouldHaveLabel(String title, Duration... duration);
}
