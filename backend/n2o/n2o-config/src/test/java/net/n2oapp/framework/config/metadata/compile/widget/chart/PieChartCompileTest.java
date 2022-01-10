package net.n2oapp.framework.config.metadata.compile.widget.chart;

import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.chart.Chart;
import net.n2oapp.framework.api.metadata.meta.widget.chart.ChartType;
import net.n2oapp.framework.api.metadata.meta.widget.chart.PieChart;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции круговой диаграммы
 */
public class PieChartCompileTest extends SourceCompileTestBase {
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
    public void testPieChart() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/chart/testPieChartCompile.page.xml")
                .get(new PageContext("testPieChartCompile"));

        PieChart pieChart1 = (PieChart) ((Chart) page.getRegions().get("single").get(0).getContent().get(0)).getComponent();
        assertThat(pieChart1.getSrc(), is("PieChart"));
        assertThat(pieChart1.getType(), is(ChartType.pie));

        assertThat(pieChart1.getComponent().getValueFieldId(), is("valueField"));
        assertThat(pieChart1.getComponent().getNameFieldId(), is("name"));
        assertThat(pieChart1.getComponent().getTooltipFieldId(), is("tooltip"));
        assertThat(pieChart1.getComponent().getCenterX(), is(100));
        assertThat(pieChart1.getComponent().getCenterY(), is(150));
        assertThat(pieChart1.getComponent().getInnerRadius(), is(15));
        assertThat(pieChart1.getComponent().getOuterRadius(), is(50));
        assertThat(pieChart1.getComponent().getStartAngle(), is(30));
        assertThat(pieChart1.getComponent().getEndAngle(), is(180));
        assertThat(pieChart1.getComponent().getColor(), is("#8884d8"));
        assertThat(pieChart1.getComponent().getHasLabel(), is(true));

        PieChart pieChart2 = (PieChart) ((Chart) page.getRegions().get("single").get(0).getContent().get(1)).getComponent();
        assertThat(pieChart2.getComponent().getInnerRadius(), is(0));
        assertThat(pieChart2.getComponent().getStartAngle(), is(0));
        assertThat(pieChart2.getComponent().getEndAngle(), is(360));
        assertThat(pieChart2.getComponent().getHasLabel(), is(false));
    }
}

