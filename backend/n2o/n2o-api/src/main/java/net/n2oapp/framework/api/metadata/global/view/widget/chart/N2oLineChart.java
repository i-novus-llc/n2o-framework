package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель линейного графика
 */
@Getter
@Setter
public class N2oLineChart extends N2oStandardChart {
    private String dataKey;
    private String color;
}
