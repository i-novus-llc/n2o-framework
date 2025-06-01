package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.chart.*;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции линейного графика
 */
class LineChartCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oChartsPack(), new N2oAllDataPack());
    }

    @Test
    void testLineChart() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/chart/testLineChartCompile.page.xml")
                .get(new PageContext("testLineChartCompile"));
        Chart chart = (Chart) page.getWidget();
        LineChart lineChart = (LineChart) chart.getComponent();
        assertThat(lineChart.getSrc(), is("LineChart"));
        assertThat(lineChart.getType(), is(ChartTypeEnum.LINE));

        List<LineChartItem> items = lineChart.getItems();
        assertThat(items.size(), is(2));

        assertThat(items.get(0).getFieldId(), is("test1"));
        assertThat(items.get(0).getLabel(), is("name"));
        assertThat(items.get(0).getColor(), is("#8884d8"));
        assertThat(items.get(0).getType(), is(ChartLineTypeEnum.MONOTONE));
        assertThat(items.get(0).getHasLabel(), is(true));

        assertThat(items.get(1).getFieldId(), is("test2"));
        assertThat(items.get(1).getType(), is(ChartLineTypeEnum.LINEAR));
        assertThat(items.get(1).getHasLabel(), is(false));
    }
}
