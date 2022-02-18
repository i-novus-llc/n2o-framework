package net.n2oapp.framework.config.io.widget.v5.chart;

import net.n2oapp.framework.config.io.widget.v4.charts.LineChartIOv4;
import net.n2oapp.framework.config.io.widget.v5.ChartWidgetIOv5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирования чтения и записи линейного графика
 */
public class LineChartXmlIOv5Test {
    @Test
    public void testLineChartXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv5(), new LineChartIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testLineChartIOv5.widget.xml");
    }
}
