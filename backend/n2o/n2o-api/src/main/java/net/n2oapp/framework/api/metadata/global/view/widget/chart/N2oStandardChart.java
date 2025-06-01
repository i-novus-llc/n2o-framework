package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
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
    @RequiredArgsConstructor
    @Getter
    public enum XAxisPositionEnum implements N2oEnum {
        TOP("top"),
        BOTTOM("bottom");

        private final String id;
    }

    /**
     * Положение оси y
     */
    @RequiredArgsConstructor
    @Getter
    public enum YAxisPositionEnum implements N2oEnum {
        LEFT("left"),
        RIGHT("right");

        private final String id;
    }
}
