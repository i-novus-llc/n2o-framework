package net.n2oapp.framework.config.io.widget.chart.v5.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oPieChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента круговой диаграммы версии 5.0
 */
@Component
public class PieChartIOv5 extends AbstractChartIOv5<N2oPieChart> {

    @Override
    public void io(Element e, N2oPieChart c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeInteger(e, "center-x", c::getCenterX, c::setCenterX);
        p.attributeInteger(e, "center-y", c::getCenterY, c::setCenterY);
        p.attributeInteger(e, "inner-radius", c::getInnerRadius, c::setInnerRadius);
        p.attributeInteger(e, "outer-radius", c::getOuterRadius, c::setOuterRadius);
        p.attributeInteger(e, "start-angle", c::getStartAngle, c::setStartAngle);
        p.attributeInteger(e, "end-angle", c::getEndAngle, c::setEndAngle);
        p.attribute(e, "name-field-id", c::getNameFieldId, c::setNameFieldId);
        p.attribute(e, "value-field-id", c::getValueFieldId, c::setValueFieldId);
        p.attribute(e, "tooltip-field-id", c::getTooltipFieldId, c::setTooltipFieldId);
        p.attribute(e, "color", c::getColor, c::setColor);
        p.attributeBoolean(e, "has-label", c::getHasLabel, c::setHasLabel);
    }

    @Override
    public Class<N2oPieChart> getElementClass() {
        return N2oPieChart.class;
    }

    @Override
    public String getElementName() {
        return "pie" ;
    }
}
