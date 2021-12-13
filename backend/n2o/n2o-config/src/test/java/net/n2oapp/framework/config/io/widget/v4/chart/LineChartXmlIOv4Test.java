package net.n2oapp.framework.config.io.widget.v4.chart;

import net.n2oapp.framework.config.io.widget.v4.charts.LineChartIOv4;
import net.n2oapp.framework.config.io.widget.v4.ChartWidgetIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирования чтения и записи линейного графика
 */
public class LineChartXmlIOv4Test {
    @Test
    public void testLineChartXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv4(), new LineChartIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testLineChartIOv4.widget.xml");
    }
}
