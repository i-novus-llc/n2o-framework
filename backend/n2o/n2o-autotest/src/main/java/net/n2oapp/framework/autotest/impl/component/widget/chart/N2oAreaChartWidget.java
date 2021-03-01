package net.n2oapp.framework.autotest.impl.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.widget.chart.Area;
import net.n2oapp.framework.autotest.api.component.widget.chart.AreaChartWidget;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

/**
 * Виджет - диаграмма-область для автотестирования
 */
public class N2oAreaChartWidget extends N2oChartWidget implements AreaChartWidget {

    @Override
    public Area area(int index) {
        return component(element().$$(".recharts-layer.recharts-area").get(index), Area.class);
    }
}
