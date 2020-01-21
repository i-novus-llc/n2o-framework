package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLegendIconType;

/**
 * Исходная модель компонента стандартной диаграммы
 */
@Getter
@Setter
public class N2oStandardChart extends N2oAbstractChart {
    private String xAxisFieldId;
    private XAxisOrientationType xAxisOrientation;
    private Boolean xHasLabel;
    private String yAxisFieldId;
    private YAxisOrientationType yAxisOrientation;
    private Boolean yHasLabel;
    private String gridStrokeDashArray;
    private Boolean gridHorizontal;
    private Boolean gridVertical;
    private String tooltipSeparator;
    private ChartLegendIconType legendIconType;

    /**
     * Положение оси X
     */
    public enum XAxisOrientationType {
        top,
        bottom
    }

    /**
     * Положение оси y
     */
    public enum YAxisOrientationType {
        left,
        right
    }
}
