package net.n2oapp.framework.config.io.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oChart;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.Display;
import net.n2oapp.framework.api.metadata.global.view.widget.chart.N2oChartValue;
import net.n2oapp.framework.config.persister.widget.N2oChartPersister;
import net.n2oapp.framework.config.reader.widget.BaseWidgetReaderTest;
import net.n2oapp.framework.config.reader.widget.widget3.N2oChartReaderV3;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import org.junit.Before;
import org.junit.Test;

/**
 * @author V. Alexeev.
 * @date 03.03.2016
 */
public class ChartXmlIOTest extends BaseWidgetReaderTest {

    private SelectiveReader reader;

    private SelectivePersister persister;

    private ION2oMetadataTester tester;

    @Before
    public void setUp() throws Exception {
        reader = new SelectiveReader().addReader(new N2oChartReaderV3());
        persister = new SelectivePersister().addPersister(new N2oChartPersister());
        tester = new ION2oMetadataTester()
                .addReader(reader)
                .addPersister(persister);
    }

    @Test
    public void testChartIO() {
        assert tester.check("net/n2oapp/framework/config/reader/widget/chart/testChartReader1.widget.xml",
                (N2oChart n2oChart) -> {
                    assertWidgetAttribute(n2oChart);
                    assert n2oChart.getDisplay().equals(Display.POLAR);
                    assert n2oChart.getLabelFieldId().equals("test123");
                    assert n2oChart.getValueFieldId().equals("test13");
                });

        assert tester.check("net/n2oapp/framework/config/reader/widget/chart/testChartReader2.widget.xml",
                (N2oChart n2oChart) -> {
                    assertWidgetAttribute(n2oChart);
                    assert n2oChart.getDisplay().equals(Display.LINE);
                    assert n2oChart.getLabelFieldId().equals("test123");
                    assert n2oChart.getValues().length == 2;
                    for (N2oChartValue v : n2oChart.getValues()) {
                        assert v.getColor().equals("red");
                        assert v.getFieldId().equals("test");
                    }
                });

    }

}
