package net.n2oapp.framework.autotest.widget.charts;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.chart.Pie;
import net.n2oapp.framework.autotest.api.component.widget.chart.PieChartWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PieChartAT extends AutoTestBase {
    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        setResourcePath("net/n2oapp/framework/autotest/widget/chart/pie");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/pie/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/pie/pie.query.xml"));
    }

    @Test
    void testPie() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        PieChartWidget pieChart = page.regions().region(0, SimpleRegion.class).content().widget(PieChartWidget.class);

        pieChart.shouldHaveHeight(250);
        pieChart.shouldHaveWidth(730);

        Pie pie = pieChart.pie();

        pie.shouldHaveSectorsCount(6);

        pie.shouldHaveSectorColor("#82ca9d");

        pie.shouldHaveLabels();
        pie.shouldHaveLabelText("Group A");
        pie.shouldHaveLabelText("Group B");
        pie.shouldHaveLabelText("Group C");
        pie.shouldHaveLabelText("Group D");
        pie.shouldHaveLabelText("Group E");
        pie.shouldHaveLabelText("Group F");
    }
}