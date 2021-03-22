package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLineType;

/**
 * Исходная модель области диаграммы-области
 */
@Getter
@Setter
public class N2oAreaChartItem extends N2oStandardChartItem {
    private ChartLineType lineType;
    private String strokeColor;
}
