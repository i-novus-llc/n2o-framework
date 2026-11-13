package net.n2oapp.framework.autotest.impl.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.widget.chart.Line;
import net.n2oapp.framework.autotest.api.component.widget.chart.LineChartWidget;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

public class N2oLineChartWidget extends N2oChartWidget implements LineChartWidget {
    @Override
    public Line line(int index) {
        return component(element().$$(".recharts-layer.recharts-line").get(index), Line.class);
    }
}