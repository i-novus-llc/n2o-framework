package net.n2oapp.framework.config.io.widget.chart;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oPieChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись круговой диаграммы
 */
@Component
public class PieChartIOv4 extends AbstractChartIOv4<N2oPieChart> {

    @Override
    public void io(Element e, N2oPieChart c, IOProcessor p) {
        super.io(e, c, p);
        p.attributeInteger(e, "cx", c::getCx, c::setCx);
        p.attributeInteger(e, "cy", c::getCy, c::setCy);
        p.attributeInteger(e, "inner-radius", c::getInnerRadius, c::setInnerRadius);
        p.attributeInteger(e, "outer-radius", c::getOuterRadius, c::setOuterRadius);
        p.attributeInteger(e, "start-angle", c::getStartAngle, c::setStartAngle);
        p.attributeInteger(e, "end-angle", c::getEndAngle, c::setEndAngle);
        p.attribute(e, "name-key", c::getNameKey, c::setNameKey);
        p.attribute(e, "data-key", c::getDataKey, c::setDataKey);
        p.attribute(e, "color", c::getColor, c::setColor);
    }

    @Override
    public String getElementName() {
        return "pie" ;
    }

    @Override
    public Class<N2oPieChart> getElementClass() {
        return N2oPieChart.class;
    }
}
