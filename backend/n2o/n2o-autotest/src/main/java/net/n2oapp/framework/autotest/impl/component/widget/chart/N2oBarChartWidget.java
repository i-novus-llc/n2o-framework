package net.n2oapp.framework.autotest.impl.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.widget.chart.Bar;
import net.n2oapp.framework.autotest.api.component.widget.chart.BarChartWidget;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

public class N2oBarChartWidget extends N2oChartWidget implements BarChartWidget {
    @Override
    public Bar bar(int index) {
        return component(element().$$(".recharts-layer.recharts-bar").get(index), Bar.class);
    }
}