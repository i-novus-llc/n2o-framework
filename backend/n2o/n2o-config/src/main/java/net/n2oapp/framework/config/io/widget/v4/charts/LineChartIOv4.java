package net.n2oapp.framework.config.io.widget.v4.charts;

import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oLineChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oLineChartItem;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLineType;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись компонента линейного графика
 */
@Component
public class LineChartIOv4 extends StandardChartIOv4<N2oLineChart> {

    @Override
    public void io(Element e, N2oLineChart c, IOProcessor p) {
        super.io(e, c, p);
        p.children(e, null, "line", c::getItems, c::setItems, N2oLineChartItem.class, this::lineChartIOv4);
    }

    private void lineChartIOv4(Element e, N2oLineChartItem i, IOProcessor p) {
        item(e, i, p);
        p.attributeEnum(e, "type", i::getType, i::setType, ChartLineType.class);
    }

    @Override
    public Class<N2oLineChart> getElementClass() {
        return N2oLineChart.class;
    }

    @Override
    public String getElementName() {
        return "lines";
    }
}
