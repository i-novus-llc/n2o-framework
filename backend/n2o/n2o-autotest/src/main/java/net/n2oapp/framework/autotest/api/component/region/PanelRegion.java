package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.Expandable;

/**
 * Регион в виде панели для автотестирования
 */
public interface PanelRegion extends Region, Expandable {
    /**
     * @return Элемент региона (виджет/регион) для автотестирования
     */
    RegionItems content();

    /**
     * Проверка заголовка на соответствие
     * @param title ожидаемый заголовок
     */
    void shouldHaveTitle(String title);

    /**
     * Проверка отсутствия заголовка
     */
    void shouldNotHaveTitle();

    /**
     * Проверка заголовка подвала
     * @param footer ожидаемый заголовок
     */
    void shouldHaveFooterTitle(String footer);

    /**
     * Проверка наличия возможности скрыть регион
     */
    void shouldBeCollapsible();

    /**
     * Проверка отсутствия возможности скрыть регион
     */
    void shouldNotBeCollapsible();

    /**
     * Проверка цвета границы
     * @param color ожидаемый цвет
     */
    void shouldHaveBorderColor(Colors color);

    /**
     * Проверка иконки
     * @param icon ожидаемая иконка
     */
    void shouldHaveIcon(String icon);
}
