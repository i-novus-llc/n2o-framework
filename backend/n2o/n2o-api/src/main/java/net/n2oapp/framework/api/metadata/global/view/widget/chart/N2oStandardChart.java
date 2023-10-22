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
    private String xFieldId;
    private XAxisPosition xPosition;
    private Boolean xHasLabel;
    private String yFieldId;
    private YAxisPosition yPosition;
    private Boolean yHasLabel;
    private Integer yMin;
    private Integer yMax;
    private String gridStrokeDasharray;
    private Boolean gridHorizontal;
    private Boolean gridVertical;
    private String tooltipSeparator;
    private ChartLegendIconType legendIconType;

    /**
     * Положение оси X
     */
    public enum XAxisPosition {
        top,
        bottom
    }

    /**
     * Положение оси y
     */
    public enum YAxisPosition {
        left,
        right
    }
}
