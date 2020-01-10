package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель стандартной диаграммы
 */
@Getter
@Setter
public abstract class N2oStandardChart extends N2oChart {
    private String xAxisDataKey;
    private Integer xAxisTickCount;
    private String xAxisLabel;
    private String yAxisDataKey;
    private Integer yAxisTickCount;
    private String yAxisLabel;
    private Integer gridX;
    private Integer gridY;
    private Integer gridWidth;
    private Integer gridHeight;
    private Boolean gridHorizontalEnabled;
    private Boolean gridVerticalEnabled;
    private String tooltipSeparator;
    private String tooltipLabel;
    private Integer legendWidth;
    private Integer legendHeight;
}
