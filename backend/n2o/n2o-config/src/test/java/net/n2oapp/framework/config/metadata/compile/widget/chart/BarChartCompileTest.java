package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.meta.widget.chart.*;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.page.SimplePageElementIOv2;
import net.n2oapp.framework.config.io.widget.chart.ChartWidgetIOv4;
import net.n2oapp.framework.config.io.widget.chart.charts.BarChartIOv4;
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
 * Тестирование компиляции гистограммы
 */
public class BarChartCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.compilers(new SimplePageCompiler(), new ChartCompiler(), new BarChartCompiler());
        builder.ios(new SimplePageElementIOv2(), new ChartWidgetIOv4(), new BarChartIOv4());
    }

    @Test
    public void testBarChart() {
        Chart chart = (Chart) compile("net/n2oapp/framework/config/metadata/compile/widgets/chart/testBarChartCompile.widget.xml")
                .get(new WidgetContext("testBarChartCompile"));

        BarChart barChart = (BarChart) chart.getComponent();
        assertThat(barChart.getSrc(), is("BarChart"));
        assertThat(barChart.getType(), is(ChartType.bar));

        List<BarChartItem> items = barChart.getItems();
        assertThat(items.size(), is(2));

        assertThat(items.get(0).getFieldId(), is("test1"));
        assertThat(items.get(0).getLabel(), is("name"));
        assertThat(items.get(0).getColor(), is("#8884d8"));
        assertThat(items.get(0).getHasLabel(), is(true));

        assertThat(items.get(1).getFieldId(), is("test2"));
        assertThat(items.get(1).getHasLabel(), is(false));
    }
}
