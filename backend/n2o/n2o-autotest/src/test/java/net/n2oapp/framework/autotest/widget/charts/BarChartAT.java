package net.n2oapp.framework.autotest.widget.charts;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.chart.BarChartWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для виджета Гистограмма
 */

class BarChartAT extends AutoTestBase {
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
        setResourcePath("net/n2oapp/framework/autotest/widget/chart/bars");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/bars/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/bars/bar.query.xml"));
    }

    @Test
    void testBar() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        BarChartWidget barChart = page.regions().region(0, SimpleRegion.class).content().widget(BarChartWidget.class);

        barChart.shouldHaveHeight(250);
        barChart.shouldHaveWidth(730);

        barChart.shouldHaveXAxis();
        barChart.shouldHaveYAxis();
        barChart.shouldHaveLegend();
        barChart.shouldHaveGrid();

        barChart.shouldHaveChartWidth(660);
        barChart.shouldHaveChartHeight(186);

        barChart.shouldHaveLegendItem(0, "expenses");
        barChart.shouldHaveLegendItem(1, "income");

        barChart.shouldHaveXAxisTick(0, "January");
        barChart.shouldHaveXAxisTick(1, "February");
        barChart.shouldHaveXAxisTick(2, "March");
        barChart.shouldHaveXAxisTick(3, "April");
        barChart.shouldHaveXAxisTick(4, "May");
        barChart.shouldHaveXAxisTick(5, "June");
        barChart.shouldHaveXAxisTicksCount(6);

        barChart.shouldHaveYAxisTick(0, "0");
        barChart.shouldHaveYAxisTick(1, "2500");
        barChart.shouldHaveYAxisTick(2, "5000");
        barChart.shouldHaveYAxisTick(3, "7500");
        barChart.shouldHaveYAxisTick(4, "10000");
        barChart.shouldHaveYAxisTicksCount(5);

        barChart.bar(0).shouldHaveColor("#8884d8");
        barChart.bar(1).shouldHaveColor("#82ca9d");
    }
}