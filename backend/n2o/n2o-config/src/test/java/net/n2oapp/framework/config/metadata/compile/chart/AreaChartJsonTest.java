package net.n2oapp.framework.config.metadata.compile.chart;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.widget.ChartCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.chart.AreaChartCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oChartV4IOPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class AreaChartJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oChartV4IOPack(), new N2oWidgetsPack());
        builder.compilers(new ChartCompiler(), new AreaChartCompiler());
    }

    @Test
    public void areaChart() {
        check("net/n2oapp/framework/config/metadata/compile/chart/testAreaChart.widget.xml",
                "components/widgets/Chart/json/AreaChart.meta.json")
                .cutJson("Page_Chart.chart")
                .cutXml("chart")
                .exclude("src", "margin", "autoFocus", "fetchOnInit", "layout", "stackOffset", "baseValue",
                        "XAxis.hide", "XAxis.height", "XAxis.orientation", "XAxis.type", "XAxis.allowDecimals",
                        "XAxis.allowDataOverflow", "XAxis.allowDuplicatedCategory", "XAxis.interval", "XAxis.padding",
                        "XAxis.minTickGap", "XAxis.axisLine", "XAxis.tickLine", "XAxis.tickSize", "XAxis.label",
                        "YAxis.hide", "YAxis.height", "YAxis.orientation", "YAxis.type", "YAxis.allowDecimals",
                        "YAxis.allowDataOverflow", "YAxis.allowDuplicatedCategory", "YAxis.interval", "YAxis.padding",
                        "YAxis.minTickGap", "YAxis.axisLine", "YAxis.tickLine", "YAxis.tickSize", "YAxis.label",
                        "cartesianGrid.horizontal", "cartesianGrid.vertical", "cartesianGrid.horizontalPoints",
                        "cartesianGrid.verticalPoints", "cartesianGrid.strokeDasharray",
                        "tooltip.offset", "tooltip.filterNull", "tooltip.itemStyle", "tooltip.wrapperStyle",
                        "tooltip.contentStyle", "tooltip.labelStyle", "tooltip.viewBox", "tooltip.label",
                        "legend.layout", "legend.align", "legend.verticalAlign", "legend.iconSize",
                        "legend.iconType", "legend.margin", "legend.wrapperStyle",
                        "areas[0].type", "areas[0].legendType", "areas[0].dot", "areas[0].activeDot", "areas[0].label",
                        "areas[0].stroke", "areas[0].layout", "areas[1].type", "areas[1].legendType", "areas[1].dot",
                        "areas[1].activeDot", "areas[1].label", "areas[1].stroke", "areas[1].layout", "areas[2].type",
                        "areas[2].legendType", "areas[2].dot", "areas[2].activeDot", "areas[2].label", "areas[2].stroke", "areas[2].layout"
                        )
                .assertEquals();
    }
}
