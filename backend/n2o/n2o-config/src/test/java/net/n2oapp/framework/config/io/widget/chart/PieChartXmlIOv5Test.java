package net.n2oapp.framework.config.io.widget.chart;

import net.n2oapp.framework.config.io.widget.charts.PieChartIOv5;
import net.n2oapp.framework.config.io.widget.ChartWidgetIOv5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирования чтения и записи круговой диаграммы
 */
class PieChartXmlIOv5Test {
    
    @Test
    void testPieChartXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv5(), new PieChartIOv5());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testPieChartIOv5.widget.xml");
    }
}
