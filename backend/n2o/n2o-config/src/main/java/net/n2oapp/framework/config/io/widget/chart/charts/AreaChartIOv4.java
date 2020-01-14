package net.n2oapp.framework.config.io.widget.chart.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oAreaChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись диаграммы-области
 */
@Component
public class AreaChartIOv4 extends StandardChartIOV4<N2oAreaChart> {
    @Override
    public void io(Element e, N2oAreaChart c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "stack-id", c::getStackId, c::setStackId);
        p.attribute(e, "data-key", c::getDataKey, c::setDataKey);
        p.attribute(e, "color", c::getColor, c::setColor);
    }

    @Override
    public Class<N2oAreaChart> getElementClass() {
        return N2oAreaChart.class;
    }

    @Override
    public String getElementName() {
        return "area";
    }
}
