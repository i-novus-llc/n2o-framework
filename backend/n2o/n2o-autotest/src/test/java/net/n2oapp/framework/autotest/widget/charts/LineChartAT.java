package net.n2oapp.framework.autotest.widget.charts;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.chart.LineChartWidget;
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
 * Автотест для виджета Линейный график
 */
class LineChartAT extends AutoTestBase {
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
        setResourcePath("net/n2oapp/framework/autotest/widget/chart/lines");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/lines/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/lines/line.query.xml"));
    }

    @Test
    void testLine() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        LineChartWidget lineChartWidget = page.regions().region(0, SimpleRegion.class).content().widget(LineChartWidget.class);

        lineChartWidget.shouldHaveHeight(250);
        lineChartWidget.shouldHaveWidth(730);

        lineChartWidget.shouldHaveXAxis();
        lineChartWidget.shouldHaveYAxis();
        lineChartWidget.shouldHaveLegend();
        lineChartWidget.shouldHaveGrid();

        lineChartWidget.shouldHaveChartWidth(660);
        lineChartWidget.shouldHaveChartHeight(186);

        lineChartWidget.shouldHaveLegendItem(0, "expenses");
        lineChartWidget.shouldHaveLegendItem(1, "income");

        lineChartWidget.shouldHaveXAxisTick(0, "January");
        lineChartWidget.shouldHaveXAxisTick(1, "February");
        lineChartWidget.shouldHaveXAxisTick(2, "March");
        lineChartWidget.shouldHaveXAxisTick(3, "April");
        lineChartWidget.shouldHaveXAxisTick(4, "May");
        lineChartWidget.shouldHaveXAxisTick(5, "June");
        lineChartWidget.shouldHaveXAxisTicksCount(6);

        lineChartWidget.shouldHaveYAxisTick(0, "0");
        lineChartWidget.shouldHaveYAxisTick(1, "2500");
        lineChartWidget.shouldHaveYAxisTick(2, "5000");
        lineChartWidget.shouldHaveYAxisTick(3, "7500");
        lineChartWidget.shouldHaveYAxisTick(4, "10000");
        lineChartWidget.shouldHaveYAxisTicksCount(5);

        lineChartWidget.line(0).shouldHaveColor("#8884d8");
        lineChartWidget.line(1).shouldHaveColor("#82ca9d");
    }
}