package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartLegendIconTypeEnum;
import net.n2oapp.framework.api.metadata.meta.widget.chart.LineChart;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции стандартной диаграммы
 */
class StandardChartCompileTest extends SourceCompileTestBase {
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
    void testStandardChart() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/chart/testStandardChartCompile.page.xml")
                .get(new PageContext("testStandardChartCompile"));

        LineChart chart1 = (LineChart) ((Chart) page.getRegions().get("single").get(0).getContent().get(0)).getComponent();
        assertThat(chart1.getXAxis().getFieldId(), is("xField"));
        assertThat(chart1.getXAxis().getPosition(), is("top"));
        assertThat(chart1.getXAxis().getHasLabel(), is(true));
        assertThat(chart1.getYAxis().getFieldId(), is("yField"));
        assertThat(chart1.getYAxis().getPosition(), is("right"));
        assertThat(chart1.getYAxis().getHasLabel(), is(true));
        assertThat(chart1.getYAxis().getMin(), is(100));
        assertThat(chart1.getYAxis().getMax(), is(1000));
        assertThat(chart1.getGrid().getStrokeDashArray(), is("3 3"));
        assertThat(chart1.getGrid().getHorizontal(), is(false));
        assertThat(chart1.getGrid().getVertical(), is(false));
        assertThat(chart1.getLegend().getIconType(), is(ChartLegendIconTypeEnum.diamond));
        assertThat(chart1.getTooltip().getSeparator(), is(":"));

        LineChart chart2 = (LineChart) ((Chart) page.getRegions().get("single").get(0).getContent().get(1)).getComponent();
        assertThat(chart2.getXAxis().getPosition(), is("bottom"));
        assertThat(chart2.getXAxis().getHasLabel(), is(false));
        assertThat(chart2.getYAxis().getPosition(), is("left"));
        assertThat(chart2.getYAxis().getHasLabel(), is(false));
        assertThat(chart2.getGrid().getHorizontal(), is(true));
        assertThat(chart2.getGrid().getVertical(), is(true));
        assertThat(chart2.getLegend().getIconType(), is(ChartLegendIconTypeEnum.line));
        assertThat(chart2.getTooltip().getSeparator(), is(" "));
    }
}
