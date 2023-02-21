package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.widget.Widget;

/**
 * Виджет - Диаграмма для автотестирования
 */
public interface ChartWidget extends Widget {

    /**
     * Проверка ширины виджета
     * @param width ожидаемая ширина виджета
     */
    void shouldHaveWidth(int width);

    /**
     * Проверка высоты виджета
     * @param height ожидаемая высота виджета
     */
    void shouldHaveHeight(int height);
}
