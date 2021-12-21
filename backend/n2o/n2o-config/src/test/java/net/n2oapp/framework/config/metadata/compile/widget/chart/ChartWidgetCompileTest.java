package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v4.ChartWidgetIOv4;
import net.n2oapp.framework.config.io.widget.v4.charts.PieChartIOv4;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.widget.ChartCompiler;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции виджета Диаграмма
 */
public class ChartWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.compilers(new ChartCompiler(), new PieChartCompiler());
        builder.ios(new ChartWidgetIOv4(), new PieChartIOv4());
    }

    @Test
    public void testChart() {
        Chart chart = (Chart) compile("net/n2oapp/framework/config/metadata/compile/widgets/chart/testChartCompile.widget.xml")
                .get(new WidgetContext("testChartCompile"));

        assertThat(chart.getSrc(), is("ChartWidget"));
        assertThat(chart.getName(), is("testChart"));
        assertThat(chart.getComponent().getWidth(), is(600));
        assertThat(chart.getComponent().getHeight(), is(400));
        assertThat(chart.getComponent().getSize(), is(20));
        assertThat(chart.getName(), is("testChart"));
        assertThat(chart.getIcon(), is("icon"));
        assertThat(chart.getStyle().size(), is(1));
        assertThat(chart.getStyle().get("color"), is("red"));
    }
}

