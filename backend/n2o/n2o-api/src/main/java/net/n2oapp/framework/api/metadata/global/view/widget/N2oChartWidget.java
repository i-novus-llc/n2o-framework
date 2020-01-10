package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oChart;

/**
 * Исходная модель виджета-диаграммы
 */
@Getter
@Setter
public class N2oChartWidget extends N2oWidget {
    private N2oChart chart;
}
