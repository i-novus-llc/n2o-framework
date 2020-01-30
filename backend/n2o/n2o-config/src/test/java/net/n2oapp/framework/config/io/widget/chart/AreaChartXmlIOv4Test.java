package net.n2oapp.framework.config.io.widget.chart;

import net.n2oapp.framework.config.io.widget.chart.charts.AreaChartIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

public class AreaChartXmlIOv4Test {
    @Test
    public void testAreaChartXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv4(), new AreaChartIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testAreaChartIOv4.widget.xml");
    }
}
