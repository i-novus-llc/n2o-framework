package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.chart.BarChart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.BarChartItem;
import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartType;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oChartsPack(), new N2oAllDataPack());
    }

    @Test
    public void testBarChart() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/chart/testBarChartCompile.page.xml")
                .get(new PageContext("testBarChartCompile"));
        Chart chart = (Chart) page.getWidget();
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
