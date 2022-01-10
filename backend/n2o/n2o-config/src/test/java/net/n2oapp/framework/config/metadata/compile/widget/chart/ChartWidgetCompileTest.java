package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oChartsPack(), new N2oAllDataPack());
    }

    @Test
    public void testChart() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/chart/testChartCompile.page.xml")
                .get(new PageContext("testChartCompile"));
        Chart chart = (Chart) page.getWidget();
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

