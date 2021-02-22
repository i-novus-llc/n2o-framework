package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.widget.Widget;

/**
 * Виджет - Диаграмма для автотестирования
 */
public interface ChartWidget extends Widget {

    void shouldHaveWidth(int width);

    void shouldHaveHeight(int height);
}
