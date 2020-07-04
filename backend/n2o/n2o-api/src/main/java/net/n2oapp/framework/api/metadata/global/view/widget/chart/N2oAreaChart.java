package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель компонента круговой диаграммы
 */
@Getter
@Setter
public class N2oAreaChart extends N2oStandardChart {
    private N2oAreaChartItem[] items;
}
