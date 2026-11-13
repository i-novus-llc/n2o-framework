package net.n2oapp.framework.autotest.impl.component.widget.chart;

import net.n2oapp.framework.autotest.api.component.widget.chart.Pie;
import net.n2oapp.framework.autotest.api.component.widget.chart.PieChartWidget;

import static net.n2oapp.framework.autotest.N2oSelenide.component;

public class N2oPieChartWidget extends N2oChartWidget implements PieChartWidget {
    @Override
    public Pie pie() {
        return component(element().$(".recharts-layer.recharts-pie"), Pie.class);
    }
}