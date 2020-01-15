package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель компонента стандартной диаграммы
 */
@Getter
@Setter
public class N2oStandardChart extends N2oAbstractChart {
    private String xAxisDataKey;
    private Integer xAxisTickCount;
    private String yAxisDataKey;
    private Integer yAxisTickCount;
    private Integer gridX;
    private Integer gridY;
    private Integer gridWidth;
    private Integer gridHeight;
    private String tooltipSeparator;
    private Integer legendWidth;
    private Integer legendHeight;
}
