package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель гистограммы
 */
@Getter
@Setter
public class N2oBarChart extends N2oStandardChart {
    private String stackId;
    private String dataKey;
    private String color;
}
