package net.n2oapp.framework.autotest.api.component.region;

import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.component.Expandable;

import java.time.Duration;

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
    void shouldHaveTitle(String title, Duration... duration);

    /**
     * Проверка отсутствия заголовка
     */
    void shouldNotHaveTitle();

    /**
     * Проверка заголовка подвала
     * @param footer ожидаемый заголовок
     */
    void shouldHaveFooterTitle(String footer, Duration... duration);

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
    void shouldHaveBorderColor(ColorsEnum color);

    /**
     * Проверка иконки
     * @param icon ожидаемая иконка
     */
    void shouldHaveIcon(String icon);
}
