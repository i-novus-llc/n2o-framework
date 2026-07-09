package net.n2oapp.framework.config.io.widget.chart;

import net.n2oapp.framework.config.io.widget.ChartWidgetIOv5;
import net.n2oapp.framework.config.io.widget.charts.LineChartIOv5;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import org.junit.jupiter.api.Test;

/**
 * Тестирования чтения и записи линейного графика
 */
class LineChartXmlIOv5Test {
    
    @Test
    void testLineChartXmlIOv5Test() {
        ION2oMetadataTester tester = new ION2oMetadataTester();
        tester.ios(new ChartWidgetIOv5(), new LineChartIOv5());

        assert tester.check("net/n2oapp/framework/config/io/widget/chart/testLineChartIOv5.widget.xml");
    }
}
