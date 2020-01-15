package net.n2oapp.framework.config.metadata.compile.chart;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.widget.ChartCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.chart.BarChartCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oChartV4IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class BarChartJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oChartV4IOPack(), new N2oWidgetsPack());
        builder.compilers(new ChartCompiler(), new BarChartCompiler());
    }

    @Test
    public void barChart() {
        check("net/n2oapp/framework/config/metadata/compile/chart/testBarChart.widget.xml",
                "components/widgets/Chart/json/BarChart.meta.json")
                .cutJson("Page_Chart.chart")
                .cutXml("chart")
                .exclude("src", "margin", "autoFocus", "fetchOnInit", "layout", "stackOffset", "baseValue",
                        "barCategoryGap", "barGap", "barSize", "maxBarSize", "reverseStackOrder",
                        "XAxis.hide", "XAxis.width", "XAxis.height", "XAxis.orientation", "XAxis.type", "XAxis.allowDecimals",
                        "XAxis.allowDataOverflow", "XAxis.allowDuplicatedCategory", "XAxis.interval", "XAxis.padding",
                        "XAxis.minTickGap", "XAxis.axisLine", "XAxis.tickLine", "XAxis.tickSize", "XAxis.label",
                        "YAxis.hide", "YAxis.width", "YAxis.height", "YAxis.orientation", "YAxis.type", "YAxis.allowDecimals",
                        "YAxis.allowDataOverflow", "YAxis.allowDuplicatedCategory", "YAxis.interval", "YAxis.padding",
                        "YAxis.minTickGap", "YAxis.axisLine", "YAxis.tickLine", "YAxis.tickSize", "YAxis.label",
                        "cartesianGrid.horizontal", "cartesianGrid.vertical", "cartesianGrid.horizontalPoints",
                        "cartesianGrid.verticalPoints", "cartesianGrid.strokeDasharray",
                        "tooltip.offset", "tooltip.filterNull", "tooltip.itemStyle", "tooltip.wrapperStyle",
                        "tooltip.contentStyle", "tooltip.labelStyle", "tooltip.viewBox", "tooltip.label",
                        "legend.layout", "legend.align", "legend.verticalAlign", "legend.iconSize",
                        "legend.iconType", "legend.margin", "legend.wrapperStyle",
                        "bars[0].layout", "bars[0].legendType", "bars[0].label", "bars[0].barSize", "bars[0].maxBarSize",
                        "bars[0].background", "bars[0].stackId", "bars[1].layout", "bars[1].legendType", "bars[1].label",
                        "bars[1].barSize", "bars[1].maxBarSize", "bars[1].background", "bars[1].stackId"
                        )
                .assertEquals();
    }
}
