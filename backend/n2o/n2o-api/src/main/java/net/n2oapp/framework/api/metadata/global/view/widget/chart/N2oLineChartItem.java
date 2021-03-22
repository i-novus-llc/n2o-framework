package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLineType;

/**
 * Исходная модель линии линейного графика
 */
@Getter
@Setter
public class N2oLineChartItem extends N2oStandardChartItem {
    private ChartLineType type;
}
