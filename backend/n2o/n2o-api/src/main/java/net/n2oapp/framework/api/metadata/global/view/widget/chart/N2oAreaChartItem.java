package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель области диаграммы-области
 */
@Getter
@Setter
public class N2oAreaChartItem extends N2oStandardChartItem {
    private String dataKey;
    private String color;
}
