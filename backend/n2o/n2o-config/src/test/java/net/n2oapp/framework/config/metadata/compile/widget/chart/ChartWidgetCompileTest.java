package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции виджета Диаграмма
 */
public class ChartWidgetCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(
                new N2oPagesPack(),
                new N2oRegionsPack(),
                new N2oWidgetsPack(),
                new N2oChartsPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    void testChart() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/chart/testChartCompile.page.xml")
                .get(new PageContext("testChartCompile"));
        Chart chart = (Chart) page.getWidget();
        assertThat(chart.getSrc(), is("ChartWidget"));
        assertThat(chart.getComponent().getWidth(), is("600px"));
        assertThat(chart.getComponent().getHeight(), is("400px"));
        assertThat(chart.getComponent().getSize(), is(20));
        assertThat(chart.getStyle().size(), is(1));
        assertThat(chart.getStyle().get("color"), is("red"));
    }
}

