package net.n2oapp.framework.config.io.widget.chart;

import net.n2oapp.framework.config.io.widget.chart.charts.LineChartIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class LineChartXmlIOv4Test {
    @Test
    public void testLineChartXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv4(), new LineChartIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testLineChartIOv4.widget.xml");
    }
}
