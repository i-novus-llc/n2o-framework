package net.n2oapp.framework.config.io.widget.v4.chart;

import net.n2oapp.framework.config.io.widget.v4.charts.PieChartIOv4;
import net.n2oapp.framework.config.io.widget.v4.ChartWidgetIOv4;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.Test;

/**
 * Тестирования чтения и записи круговой диаграммы
 */
public class PieChartXmlIOv4Test {
    @Test
    public void testPieChartXmlIOv4Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv4(), new PieChartIOv4());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testPieChartIOv4.widget.xml");
    }
}
