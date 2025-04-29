package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLegendIconTypeEnum;

/**
 * Исходная модель компонента стандартной диаграммы
 */
@Getter
@Setter
public class N2oStandardChart extends N2oAbstractChart {
    private String xFieldId;
    private XAxisPositionEnum xPosition;
    private Boolean xHasLabel;
    private String yFieldId;
    private YAxisPositionEnum yPosition;
    private Boolean yHasLabel;
    private Integer yMin;
    private Integer yMax;
    private String gridStrokeDasharray;
    private Boolean gridHorizontal;
    private Boolean gridVertical;
    private String tooltipSeparator;
    private ChartLegendIconTypeEnum legendIconType;

    /**
     * Положение оси X
     */
    public enum XAxisPositionEnum {
        top,
        bottom
    }

    /**
     * Положение оси y
     */
    public enum YAxisPositionEnum {
        left,
        right
    }
}
