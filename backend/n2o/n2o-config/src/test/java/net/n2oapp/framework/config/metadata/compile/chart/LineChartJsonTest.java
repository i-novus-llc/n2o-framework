package net.n2oapp.framework.config.metadata.compile.chart;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.widget.chart.LineChartCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oChartsIOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class LineChartJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oChartsIOPack(), new N2oWidgetsPack());
        builder.compilers(new LineChartCompiler());
    }

    @Test
    public void lineChart() {
        check("net/n2oapp/framework/config/metadata/compile/chart/testLineChart.widget.xml",
                "components/widgets/Chart/json/LineChart.meta.json")
                .cutJson("Page_Chart.chart")
                .cutXml("chart")
                .exclude("src", "margin", "autoFocus", "fetchOnInit", "layout", "stackOffset", "baseValue",
                        "XAxis.hide", "XAxis.width", "XAxis.height", "XAxis.tickCount", "XAxis.type", "XAxis.allowDecimals",
                        "XAxis.allowDataOverflow", "XAxis.allowDuplicatedCategory", "XAxis.interval", "XAxis.padding",
                        "XAxis.minTickGap", "XAxis.axisLine", "XAxis.tickLine", "XAxis.tickSize",
                        "YAxis.hide", "YAxis.width", "YAxis.height", "YAxis.tickCount", "YAxis.type", "YAxis.allowDecimals",
                        "YAxis.allowDataOverflow", "YAxis.allowDuplicatedCategory", "YAxis.interval", "YAxis.padding",
                        "YAxis.minTickGap", "YAxis.axisLine", "YAxis.tickLine", "YAxis.tickSize",
                        "cartesianGrid.x", "cartesianGrid.y", "cartesianGrid.width", "cartesianGrid.height",
                        "cartesianGrid.horizontalPoints", "cartesianGrid.verticalPoints",
                        "tooltip.offset", "tooltip.filterNull", "tooltip.itemStyle", "tooltip.wrapperStyle",
                        "tooltip.contentStyle", "tooltip.labelStyle", "tooltip.viewBox", "tooltip.label",
                        "legend.width", "legend.height", "legend.layout", "legend.align", "legend.verticalAlign",
                        "legend.iconSize", "legend.margin", "legend.wrapperStyle",
                        "lines[0].legendType", "lines[0].dot", "lines[0].activeDot", "lines[0].layout",
                        "lines[1].legendType", "lines[1].dot", "lines[1].activeDot", "lines[1].layout"
                ).assertEquals();
    }
}
