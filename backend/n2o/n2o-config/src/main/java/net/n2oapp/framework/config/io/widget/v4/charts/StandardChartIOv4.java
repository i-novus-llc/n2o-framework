package net.n2oapp.framework.config.io.widget.v4.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oStandardChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oStandardChartItem;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLegendIconTypeEnum;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись стандартного компонента диаграммы
 */
@Component
public abstract class StandardChartIOv4<T extends N2oStandardChart> extends AbstractChartIOv4<T> {

    @Override
    public void io(Element e, T c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "x-field-id", c::getXFieldId, c::setXFieldId);
        p.attributeEnum(e, "x-position", c::getXPosition, c::setXPosition, N2oStandardChart.XAxisPositionEnum.class);
        p.attributeBoolean(e, "x-has-label", c::getXHasLabel, c::setXHasLabel);
        p.attribute(e, "y-field-id", c::getYFieldId, c::setYFieldId);
        p.attributeEnum(e, "y-position", c::getYPosition, c::setYPosition, N2oStandardChart.YAxisPositionEnum.class);
        p.attributeBoolean(e, "y-has-label", c::getYHasLabel, c::setYHasLabel);
        p.attributeInteger(e, "y-min", c::getYMin, c::setYMin);
        p.attributeInteger(e, "y-max", c::getYMax, c::setYMax);
        p.attribute(e, "grid-stroke-dasharray", c::getGridStrokeDasharray, c::setGridStrokeDasharray);
        p.attributeBoolean(e, "grid-horizontal", c::getGridHorizontal, c::setGridHorizontal);
        p.attributeBoolean(e, "grid-vertical", c::getGridVertical, c::setGridVertical);
        p.attribute(e, "tooltip-separator", c::getTooltipSeparator, c::setTooltipSeparator);
        p.attributeEnum(e, "legend-icon-type", c::getLegendIconType, c::setLegendIconType, ChartLegendIconTypeEnum.class);
    }

    protected void item(Element e, N2oStandardChartItem i, IOProcessor p) {
        p.attribute(e, "field-id", i::getFieldId, i::setFieldId);
        p.attribute(e, "label", i::getLabel, i::setLabel);
        p.attribute(e, "color", i::getColor, i::setColor);
        p.attributeBoolean(e, "has-label", i::getHasLabel, i::setHasLabel);
    }
}
