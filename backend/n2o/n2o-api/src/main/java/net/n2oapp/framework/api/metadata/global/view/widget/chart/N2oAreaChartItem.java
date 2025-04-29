package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLineTypeEnum;

/**
 * Исходная модель области диаграммы-области
 */
@Getter
@Setter
public class N2oAreaChartItem extends N2oStandardChartItem {
    private ChartLineTypeEnum lineType;
    private String strokeColor;
}
