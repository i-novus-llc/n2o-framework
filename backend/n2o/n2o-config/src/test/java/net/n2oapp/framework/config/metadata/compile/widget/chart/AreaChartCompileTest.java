package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.meta.widget.chart.*;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.page.v2.SimplePageElementIOv2;
import net.n2oapp.framework.config.io.widget.chart.ChartWidgetIOv4;
import net.n2oapp.framework.config.io.widget.chart.charts.AreaChartIOv4;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.page.SimplePageCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.ChartCompiler;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции диаграммы-области
 */
public class AreaChartCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.compilers(new SimplePageCompiler(), new ChartCompiler(), new AreaChartCompiler());
        builder.ios(new SimplePageElementIOv2(), new ChartWidgetIOv4(), new AreaChartIOv4());
    }

    @Test
    public void testAreaChart() {
        Chart chart = (Chart) compile("net/n2oapp/framework/config/metadata/compile/widgets/chart/testAreaChartCompile.widget.xml")
                .get(new WidgetContext("testAreaChartCompile"));

        AreaChart areaChart = (AreaChart) chart.getComponent();
        assertThat(areaChart.getSrc(), is("AreaChart"));
        assertThat(areaChart.getType(), is(ChartType.area));

        List<AreaChartItem> items = areaChart.getItems();
        assertThat(items.size(), is(2));

        assertThat(items.get(0).getFieldId(), is("test1"));
        assertThat(items.get(0).getLabel(), is("name"));
        assertThat(items.get(0).getLineType(), is(ChartLineType.monotone));
        assertThat(items.get(0).getColor(), is("#8884d8"));
        assertThat(items.get(0).getStrokeColor(), is("#aaaaaa"));
        assertThat(items.get(0).getHasLabel(), is(true));

        assertThat(items.get(1).getFieldId(), is("test2"));
        assertThat(items.get(1).getLineType(), is(ChartLineType.linear));
        assertThat(items.get(1).getHasLabel(), is(false));
    }
}
