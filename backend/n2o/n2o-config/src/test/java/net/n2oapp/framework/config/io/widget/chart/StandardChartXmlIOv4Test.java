package net.n2oapp.framework.config.io.widget.chart;

import net.n2oapp.framework.config.io.widget.chart.charts.AreaChartIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class StandardChartXmlIOv4Test {
    @Test
    public void testStandardChartXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv4(), new AreaChartIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testStandardChartIOv4.widget.xml");
    }
}
