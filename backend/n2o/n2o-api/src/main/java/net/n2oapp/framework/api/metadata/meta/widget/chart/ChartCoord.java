package net.n2oapp.framework.api.metadata.meta.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Клиентская модель оси координат на графике/диаграмме
 */
@Getter
@Setter
public class ChartCoord {
    private String dataKey;
    private Integer tickCount;
}
