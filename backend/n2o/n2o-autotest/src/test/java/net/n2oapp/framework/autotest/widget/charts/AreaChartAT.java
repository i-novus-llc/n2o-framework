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
public class AreaChartAT extends AutoTestBase {
    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/area/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/chart/area/area.query.xml"));
    }

    @Test
    public void testArea() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        AreaChartWidget areaChartWidget = page.widget(AreaChartWidget.class);
        areaChartWidget.shouldHaveHeight(250);
        areaChartWidget.shouldHaveWidth(730);

        Area area1 = areaChartWidget.area(0);
        area1.shouldHaveHeight(186);
        area1.shouldHaveWidth(660);
    }
}
