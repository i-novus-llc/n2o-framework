package net.n2oapp.framework.autotest.api.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;

/**
 * Виджет - Диаграмма для автотестирования
 */
public interface ChartWidget extends StandardWidget {

    void shouldHaveWidth(String width);

    void shouldHaveHeight(String height);

}
