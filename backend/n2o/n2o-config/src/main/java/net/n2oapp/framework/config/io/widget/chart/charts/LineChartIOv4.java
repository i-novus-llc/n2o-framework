package net.n2oapp.framework.config.io.widget.chart.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oLineChart;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись линейного графика
 */
@Component
public class LineChartIOv4 extends StandardChartIOV4<N2oLineChart> {
    @Override
    public void io(Element e, N2oLineChart c, IOProcessor p) {
        super.io(e, c, p);
        p.attribute(e, "data-key", c::getDataKey, c::setDataKey);
        p.attribute(e, "color", c::getColor, c::setColor);
    }

    @Override
    public Class<N2oLineChart> getElementClass() {
        return N2oLineChart.class;
    }

    @Override
    public String getElementName() {
        return "line";
    }
}
