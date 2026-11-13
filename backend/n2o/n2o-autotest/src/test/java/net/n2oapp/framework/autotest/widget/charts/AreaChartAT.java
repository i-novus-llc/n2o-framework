package net.n2oapp.framework.autotest.widget.charts;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.chart.Area;
import net.n2oapp.framework.autotest.api.component.widget.chart.AreaChartWidget;
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
 * Автотест для виджета Диаграмма-область
 */
class AreaChartAT extends AutoTestBase {

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
        setResourcePath("net/n2oapp/framework/autotest/widget/chart/area");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/area/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/area/area.query.xml"));
    }

    @Test
    void testArea() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        AreaChartWidget areaChartWidget = page.widget(AreaChartWidget.class);

        areaChartWidget.shouldHaveHeight(250);
        areaChartWidget.shouldHaveWidth(730);

        areaChartWidget.shouldHaveXAxis();
        areaChartWidget.shouldHaveYAxis();
        areaChartWidget.shouldHaveLegend();
        areaChartWidget.shouldHaveGrid();

        areaChartWidget.shouldHaveChartWidth(660);
        areaChartWidget.shouldHaveChartHeight(186);

        areaChartWidget.shouldHaveLegendItem(0, "expenses");
        areaChartWidget.shouldHaveLegendItem(1, "income");

        areaChartWidget.shouldHaveXAxisTick(0, "January");
        areaChartWidget.shouldHaveXAxisTick(1, "February");
        areaChartWidget.shouldHaveXAxisTick(2, "March");
        areaChartWidget.shouldHaveXAxisTick(3, "April");
        areaChartWidget.shouldHaveXAxisTick(4, "May");
        areaChartWidget.shouldHaveXAxisTick(5, "June");
        areaChartWidget.shouldHaveXAxisTicksCount(6);

        areaChartWidget.shouldHaveYAxisTick(0, "0");
        areaChartWidget.shouldHaveYAxisTick(1, "2500");
        areaChartWidget.shouldHaveYAxisTick(2, "5000");
        areaChartWidget.shouldHaveYAxisTick(3, "7500");
        areaChartWidget.shouldHaveYAxisTick(4, "10000");
        areaChartWidget.shouldHaveYAxisTicksCount(5);

        Area area1 = areaChartWidget.area(0);
        area1.shouldHaveColor("#8884d8");
        area1.shouldHaveStrokeColor("#8884d8");

        Area area2 = areaChartWidget.area(1);
        area2.shouldHaveColor("#82ca9d");
        area2.shouldHaveStrokeColor("#82ca9d");
    }
}