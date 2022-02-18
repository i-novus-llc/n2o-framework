package net.n2oapp.framework.config.io.widget.v5.chart;

import net.n2oapp.framework.config.io.widget.v4.charts.PieChartIOv4;
import net.n2oapp.framework.config.io.widget.v5.ChartWidgetIOv5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирования чтения и записи круговой диаграммы
 */
public class PieChartXmlIOv5Test {
    @Test
    public void testPieChartXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv5(), new PieChartIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testPieChartIOv5.widget.xml");
    }
}
