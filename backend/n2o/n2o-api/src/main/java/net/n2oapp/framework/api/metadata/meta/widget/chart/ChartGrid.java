package net.n2oapp.framework.api.metadata.meta.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель сетки графика/диаграммы
 */
@Getter
@Setter
public class ChartGrid {
    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
}
